package com.tweetapp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cogjava729
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6158496423418719L;
	private String tweetId;
	private String username;
	private String tweetText;
	private String firstName;
	private String lastName;
	private String tweetDate;
	private Integer likesCount;
	private Integer commentsCount;
	private Boolean likeStatus;
	private List<Comment> comments = new ArrayList<>();

}
