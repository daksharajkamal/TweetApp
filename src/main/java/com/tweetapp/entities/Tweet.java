package com.tweetapp.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cogjava729
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "tweets")
public class Tweet {

	@Id
	private String tweetId;
	private String username;
	private String tweetText;
	private String firstName;
	private String lastName;
	private String tweetDate;
	private List<String> likes = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();

}
