package MysqlConnection;

//import com.ifeng.spider.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mayz on 2016/11/16.
 */
public class MysqlHelper<T> {

    private static Connection conn = MysqlDAOFactory.getInstance().getConnection();

    /**
     * 增删改【 Add 、 Del 、 Update 】
     *
     * @param sql
     * @return int
     */
    public static int executeNonQuery(String sql) {
        int result = 0;
//        Connection conn = null ;
        Statement stmt = null ;

        try {
//            conn = getConnection ();
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
        } catch (SQLException err) {
            err.printStackTrace();
            free (null , stmt, conn);
        } finally {
            free (null , stmt, conn);
        }

        return result;
    }


    /**通过反射机制查询多条记录
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public static  <T> List<T> findMoreRefResult(String sql, Object[] params,
                                         Class<T> cls )throws Exception {
//        Connection conn = null ;
        PreparedStatement pstmt = null ;
        List<T> list = new ArrayList<T>();
        ResultSet resultSet = null ;
        int index = 1;
        try {
//            conn = getConnection ();
            pstmt = conn.prepareStatement(sql);
            for(int i=0; i<params.length; i++){
                pstmt.setObject(i+1, params[i]);
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData  = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while(resultSet.next()){
                //通过反射机制创建一个实例
                T resultObject = cls.newInstance();
                for(int i = 0; i<cols_len; i++){
                    String cols_name = metaData.getColumnName(i+1);
                    Object cols_value = resultSet.getObject(cols_name);

                    try {
                        if(cols_value == null){
//                            if (cls.getDeclaredField(cols_name).getType().getTypeName().equals("int"))
                                cols_value = 0;
//                            else
//                                cols_value = null;
                        }
                        Field field = cls.getDeclaredField(cols_name);
                        field.setAccessible(true); //打开javabean的访问权限
                        field.set(resultObject, cols_value);
                    } catch (Exception e) {
                    }
                }
                list.add(resultObject);
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            Log.error("findMoreRefResult",e.toString());

        } finally {
            free (null , pstmt, conn);
        }
        return list;
    }


    public T findOne(String sql,Object[] argType,Class<T> t) {
        T obj = null;
//        Connection conn = null ;
        PreparedStatement pstmt = null ;
        ResultSet resultSet = null ;
        try {
//            conn = getConnection ();
            pstmt = conn.prepareStatement(sql);
            for(int i=0; i<argType.length; i++){
                pstmt.setObject(i+1, argType[i]);
            }
            resultSet = pstmt.executeQuery();
            Field fields[] = t.getDeclaredFields();
            ResultSetMetaData metaData  = resultSet.getMetaData();
            int cols_len = metaData.getColumnCount();
            while (resultSet.next()) {
                obj = t.newInstance();
                for(int i = 0; i<cols_len; i++){
                    String cols_name = metaData.getColumnName(i+1);
                    Object cols_value = resultSet.getObject(cols_name);
                    try {
                        if(cols_value == null){
//                            if (t.getDeclaredField(cols_name).getType().getTypeName().equals("int"))
                                cols_value = 0;
//                            else
//                                cols_value = null;
                        }
                        Field field = t.getDeclaredField(cols_name);
                        field.setAccessible(true); //打开javabean的访问权限
                        field.set(obj, cols_value);
                    } catch (Exception e) {

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            free (null , pstmt, conn);
        }
        return obj;
    }

    /**
     * 增删改【 Add 、 Delete 、 Update 】
     *
     * @param sql
     * @param obj
     * @return int
     */
    public static int executeNonQuery(String sql, Object... obj) throws Exception {
        int result = 0;
//        Connection conn = null ;
        PreparedStatement pstmt = null ;

        try {
//            conn = getConnection ();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < obj.length; i++) {
                pstmt.setObject(i + 1, obj[i]);
            }

            result = pstmt.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
            free (null , pstmt, conn);
            throw err;
        } finally {
            free (null , pstmt, conn);
        }
        return result;
    }

    /**
     * 查【 Query 】
     *
     * @param sql
     * @return ResultSet
     */
    public static ResultSet executeQuery(String sql) throws Exception{
//        Connection conn = null ;
        Statement stmt = null ;
        ResultSet rs = null ;

        try {
//            conn = getConnection ();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException err) {
            err.printStackTrace();
            free (rs, stmt, conn);
            throw err;
        }

        return rs;
    }

    /**
     * 查【 Query 】
     *
     * @param sql
     * @param obj
     * @return ResultSet
     */
    public static ResultSet executeQuery(String sql, Object... obj) {
//        Connection conn = null ;
        PreparedStatement pstmt = null ;
        ResultSet rs = null ;

        try {
//            conn = getConnection ();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < obj.length; i++) {
                pstmt.setObject(i + 1, obj[i]);
            }
//	           System.out.println(sql);
            rs = pstmt.executeQuery();
        } catch (SQLException err) {
            err.printStackTrace();
            free (rs, pstmt, conn);
        }

        return rs;
    }


    public static ArrayList ExecuteReader(String cmdtext, Object[] parms)
            throws SQLException {
        PreparedStatement pstmt = null;
//        Connection conn = null;
        ResultSet rs = null;
        try {
//            conn = getConnection();
            pstmt = conn.prepareStatement(cmdtext);

            prepareCommand(pstmt, parms);
            rs = pstmt.executeQuery();

            ArrayList al = new ArrayList();
            ResultSetMetaData rsmd = rs.getMetaData();
            int column = rsmd.getColumnCount();

            while (rs.next()) {
                Object[] ob = new Object[column];
                for (int i = 1; i <= column; i++) {
                    ob[i - 1] = rs.getObject(i);
                }
                al.add(ob);
            }
            return al;

        } catch (Exception ex) {
        }
        finally{
//            if (rs != null)
//                rs.close();
//            if (pstmt != null)
//                pstmt.close();
//            if (conn != null)
//                conn.close();
            free(rs,pstmt,conn);
        }
        return null;
    }

    /**
     * 判断记录是否存在
     *
     * @param sql
     * @return Boolean
     * @throws Exception
     */
    public static Boolean isExist(String sql) throws Exception {
        ResultSet rs = null ;

        try {
            rs = executeQuery (sql);
            rs.last();
            int count = rs.getRow();
            if (count > 0) {
                return true ;
            } else {
                return false ;
            }
        } catch (SQLException err) {
            err.printStackTrace();
            free (rs);
            return false ;
        } finally {
            free (rs);
        }
    }

    /**
     * 判断记录是否存在
     *
     * @param sql
     * @return Boolean
     */
    public static Boolean isExist(String sql, Object... obj) throws Exception{
        ResultSet rs = null ;

        try {
            rs = executeQuery (sql, obj);
            rs.last();
            int count = rs.getRow();
            if (count > 0) {
                return true ;
            } else {
                return false ;
            }
        } catch (SQLException err) {
            err.printStackTrace();
            free (rs);
            return false ;
        } finally {
            free (rs);
        }
    }

    /**
     * 获取查询记录的总行数
     *
     * @param sql
     * @return int
     * @throws Exception
     */
    public static int getCount(String sql) throws Exception {
        int result = 0;
        ResultSet rs = null ;

        try {
            rs = executeQuery (sql);
            rs.last();
            result = rs.getRow();
        } catch (SQLException err) {
            free (rs);
            err.printStackTrace();
        } finally {
            free (rs);
        }

        return result;
    }

    /**
     * 获取查询记录的总行数
     *
     * @param sql
     * @param obj
     * @return int
     */
    public static int getCount(String sql, Object... obj) {
        int result = 0;
        ResultSet rs = null ;

        try {
            rs = executeQuery (sql, obj);
            rs.last();
            result = rs.getRow();
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            free (rs);
        }

        return result;
    }

    /**
     * 释放【 ResultSet 】资源
     *
     * @param rs
     */
    public static void free(ResultSet rs) {
        try {
            if (rs != null ) {
                rs.close();
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * 释放【 Statement 】资源
     *
     * @param st
     */
    public static void free(Statement st) {
        try {
            if (st != null ) {
                st.close();
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    /**
     * 释放【 Connection 】资源
     *
     * @param conn
     */
    public static void free(Connection conn) {

        MysqlDAOFactory.getInstance().release(conn);
//        try {
//            if (conn != null ) {
//                conn.close();
//            }
//        } catch (SQLException err) {
//            err.printStackTrace();
//        }
    }

    /**
     * 释放所有数据资源
     *
     * @param rs
     * @param st
     * @param conn
     */
    public static void free(ResultSet rs, Statement st, Connection conn) {
        free (rs);
        free (st);
        free (conn);
    }

    private static void prepareCommand(PreparedStatement pstmt, Object[] parms)
            throws SQLException, UnsupportedEncodingException {
        if (parms != null && parms.length > 0) {
            for (int i = 1; i < parms.length + 1; i++) {
                Object item = parms[i - 1];
                String typeName = item.getClass().getSimpleName();
                if (typeName.equals("String")) {
                    pstmt.setString(i, item.toString());
                } else if (typeName.equals("Integer")) {
                    pstmt.setInt(i, Integer.parseInt(item.toString()));
                } else if (typeName.equals("Date")) {
                    pstmt.setDate(i, Date.valueOf(item.toString()));
                } else {
                    pstmt.setObject(i, item);
                }
            }
        }
    }

}
