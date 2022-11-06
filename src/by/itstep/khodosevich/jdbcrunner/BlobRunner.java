package by.itstep.khodosevich.jdbcrunner;

import by.itstep.khodosevich.jdbcrunner.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;

public class BlobRunner {
    public static void main(String[] args) {
        // blob binary large object (bytearray in postgreSQL)
        // clob character large object (TEXT)

getImage();
//        saveImage();
//        saveString(); // In POSTGRESQL - we set standart prepareStatement and works like string
//        saveImageForPostgresql();
    }

    /**
     * We use method saveImage() for ORACLE DB, in POSTGRESQL - blob isn't exist!!!
     */
    private static void saveImage() {
        String sql = """
                UPDATE aircraft
                 SET image = ? 
                 WHERE id = 1""";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionManager.open();
            preparedStatement = connection.prepareStatement(sql);

            connection.setAutoCommit(false);

            Blob blob = connection.createBlob();
            blob.setBytes(1, Files.readAllBytes(Path.of("resources", "Boing737.jpg")));

            preparedStatement.setBlob(1, blob);
            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private static void saveImageForPostgresql() {
        String sql = """
                UPDATE aircraft
                 SET image = ? 
                 WHERE id = 1""";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionManager.open();
            preparedStatement = connection.prepareStatement(sql);

            connection.setAutoCommit(false);

            preparedStatement.setBytes(1,
                    Files.readAllBytes(Path.of("resources", "Boing777.jpg")));

            int executeUpdate = preparedStatement.executeUpdate();
            System.out.println(executeUpdate);
            connection.commit();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    private static void saveString() {
        String sql = """
                UPDATE aircraft 
                SET model = ? 
                WHERE id = ?""";

        Connection connection = null;
        PreparedStatement clob = null;
        Long id = 1L;
        String boingName = "BOING 777";

        try {
            connection = ConnectionManager.open();
            connection.setAutoCommit(false);
            clob = connection.prepareStatement(sql);
            clob.setString(1, boingName);
            clob.setLong(2, id);
            clob.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }


    }

    private static void getImage(){
        String sql = """
                        SELECT image
                        FROM aircraft
                        WHERE id = ?
                        """;
        Long id = 1L;

        Connection connection = null;
        try{
            connection = ConnectionManager.open();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){

                byte[] image = resultSet.getBytes("image");
                Files.write(Path.of("resources", "Boing 777 new.jpg"), image, StandardOpenOption.CREATE);
            }

            connection.commit();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
