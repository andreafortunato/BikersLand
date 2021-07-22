package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.imageio.ImageIO;

import com.bikersland.Event;
import com.bikersland.User;
import com.bikersland.exceptions.EmailException;
import com.bikersland.exceptions.UsernameException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class UserDAO {
	
	public static void setUser(User user) throws SQLException, UsernameException, EmailException, IOException {
		CallableStatement stmCreateUser = DB_Connection.getConnection().prepareCall("{CALL CreateUser(?,?,?,?,?,?)}");
		
		stmCreateUser.setString(1, user.getName());
		stmCreateUser.setString(2, user.getSurname());
		stmCreateUser.setString(3, user.getUsername());
		stmCreateUser.setString(4, user.getEmail());
		stmCreateUser.setString(5, user.getPassword());
		
		if(user.getImage() != null) {
			BufferedImage buffImg = SwingFXUtils.fromFXImage(user.getImage(),null);
			ByteArrayOutputStream bts = new ByteArrayOutputStream();
			ImageIO.write(buffImg,"png" , bts);
			InputStream blobImage = new ByteArrayInputStream(bts.toByteArray());
			
			stmCreateUser.setBlob(6, blobImage);
		} else {
			stmCreateUser.setNull(6, Types.BLOB);
		}
		
		ResultSet newEvent = null;
		try {
			newEvent = stmCreateUser.executeQuery();
		} catch (SQLException ex) {
			int errorCode = ex.getErrorCode();
			if(errorCode == 1062) {
				/* Username o Email già esistenti */
				if(ex.getMessage().contains("username_UNIQUE")) {
					/* Username già presente */
					throw new UsernameException();
				} else {
					/* Email già presente */
					throw new EmailException();
				}
			}
		}
		
//		try {
//			stmt.executeUpdate(query);
//		} catch (SQLException ex) {
//			int errorCode = ex.getErrorCode();
//			if(errorCode == 1062) {
//				/* Username o Email già esistenti */
//				if(ex.getMessage().contains("username_UNIQUE")) {
//					/* Username già presente */
//					throw new UsernameException();
//				} else {
//					/* Email già presente */
//					throw new EmailException();
//				}
//			}
//		}
	}
	
	public static User askLogin (String username_email, String password) throws SQLException, IOException {
		String query = "SELECT * FROM user WHERE (username='" + username_email + "' OR email='" + username_email + "') AND password=MD5('" + password + "');";
		
		ResultSet rs;
		User user;
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		rs = stmt.executeQuery(query);
		
		if(rs.first()) {
			Image image;
			if(rs.getBinaryStream("image") != null) {
	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
	        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
			
			user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("username"),
			rs.getString("email"), null, image, rs.getDate("create_time"));
		} else {
			user = null;
		}
		
		rs.close();
		
		if (stmt != null)
			stmt.close();
		
		return user;
	}
	
	public static User getUserByUsername(String username) throws SQLException, IOException {
		User user;
		String query = "SELECT * FROM user WHERE username='" + username + "';";
		
		Statement stmt = DB_Connection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                
        ResultSet rs = stmt.executeQuery(query);
        
        if(rs.first()) {
        	Image image;
			if(rs.getBinaryStream("image") != null) {
	        	BufferedImage img = ImageIO.read(rs.getBinaryStream("image"));
	        	image = SwingFXUtils.toFXImage(img, null);
			} else {
				image = null;
			}
			
        	user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("surname"), rs.getString("username"),
        			rs.getString("email"), rs.getString("password"), image, rs.getDate("create_time"));
        } else {
        	user = null;
        }
        
        rs.close();
        
        if (stmt != null)
        	stmt.close();
        
		return user;
	}
}
