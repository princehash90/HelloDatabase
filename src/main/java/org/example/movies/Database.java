package org.example.movies;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databasePath;

    Database(String databasePath){// this will have the code will connect and set up the movie table

        this.databasePath = databasePath;

       try (Connection connection = DriverManager.getConnection(databasePath)){// setting up the connection
           //connection can be connecting over the internet or connecting to a database
           Statement statement = connection.createStatement();

           statement.execute("CREATE TABLE movies(" +
                   "id INTEGER primary key , " +// setting the id as the primary key
                   "name text, unique check ( name )>=1" +// adding unique check to check for name
                   "stars INTEGER, check (stars >=0 AND stars <=5) " +// adding a contraints to check if stars is btn 0 and 5
                   "watched BOOLEAN)");
           //this statement creates the table with column names and types.

       }catch (SQLException e){
           System.out.println("Error creating movie DB table because "+ e);
       }


    }
    public  void addNewMovies (Movie movie){// method used to add movies

        String insertSQL ="INSERT INTO movies(name, stars, watched) VALUES (?,?,?)";

        try(Connection connection =DriverManager.getConnection(databasePath)) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            //prepared statement helps reduce errors especially with quotes,data is always as data and not sql statement
            Statement statement = connection.createStatement();
            preparedStatement.setString(1,movie.getName());
            preparedStatement.setInt(2,movie.getStars());
            preparedStatement.setBoolean(3, movie.isWatched());

            preparedStatement.execute();

        }catch (SQLException e){
            System.out.println("Error adding movie "+ movie + e);
        }


    }

    public List<Movie> getAllMovies(){// medthod to get all movies
        try (Connection connection = DriverManager.getConnection(databasePath)){
            Statement statement = connection.createStatement();
            ResultSet moviesResult = statement.executeQuery("SELECT  * FROM movies ORDER BY name");

            List<Movie> movies = new ArrayList<>();

            while (moviesResult.next()){
                int id = moviesResult.getInt("id");
                String name = moviesResult.getString("name");
                int stars = moviesResult.getInt("stars");
                boolean watched = moviesResult.getBoolean("watched");
                Movie movie = new Movie(id, name,stars,watched);
                movies.add(movie);
            }
            return movies;

        }catch ( SQLException e){
            System.out.println("Error fetching all movies because "+e);
            return null;
        }

    }

    public List<Movie> getAllMoviesBYWatched(boolean watchedStatus){// method to get all movies by watched status
        try (Connection connection = DriverManager.getConnection(databasePath)){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM  movies where watched = ?");
            preparedStatement.setBoolean(1, watchedStatus);
            ResultSet movieResult = preparedStatement.executeQuery();

            List <Movie> movies =new ArrayList<>();

            while (movieResult.next()){
                int id = movieResult.getInt("id");
                String movieName = movieResult.getString("name");
                int stars = movieResult.getInt("stars");
                boolean watched = movieResult.getBoolean("watched");
                Movie movie = new Movie(id,movieName,stars,watched);
                movies.add(movie);
            }

            return movies;

        }catch (SQLException e){
            System.out.println("Error getting movies because "+ e);
            return null;
        }
    }

    public void updateMovie (Movie movie){// method to update movie
        String sql = "UPDATE movies SET stars = ?, watched = ?, WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(databasePath)){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);

            preparedStatement.setInt(1,movie.getStars());
            preparedStatement.setBoolean(2, movie.isWatched());
            preparedStatement.setInt(3, movie.getId());

            preparedStatement.execute();

        }catch (SQLException e){
            System.out.println("Error updating movie "+ movie + "because of "+ e);
        }
    }

    public void deleteMovie(Movie movie){// deleting movie method
        try (Connection connection = DriverManager.getConnection(databasePath)){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM movies WHERE id = ?");

            preparedStatement.setInt(1, movie.getId());
            preparedStatement.execute();

        }catch (SQLException e){
            System.out.println("Error deleting movie "+ movie +"Because " +e);
        }

    }

    public List<Movie> search(String searchTerm){// searching for term method

        String searchSql = "SELECT * FROM movies WHERE UPPER(name) like UPPER(?)";// ignoring case in sensitive

        try (Connection connection = DriverManager.getConnection(databasePath)){
            PreparedStatement preparedStatement = connection.prepareStatement(searchSql);
            preparedStatement.setString(1,"% " + searchTerm +"%");// this will help pull partial search
            ResultSet movieResult = preparedStatement.executeQuery();

            List<Movie> movies = new ArrayList<>();

            while (movieResult.next()){
                int id = movieResult.getInt("id");
                String name = movieResult.getString("name");
                int stars = movieResult.getInt("Stars");
                boolean watched = movieResult.getBoolean("watched");

                Movie movie= new Movie(id,name,stars,watched);
                movies.add(movie);
            }
            return movies;

        }catch (SQLException e){
            System.out.println("The Search you entered "+ searchTerm + " is not found "+ e);
            return null;
        }
    }
}
