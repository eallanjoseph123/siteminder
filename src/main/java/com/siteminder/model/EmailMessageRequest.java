package com.siteminder.model;

import com.siteminder.validator.ListEmailConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageRequest {

	@NotNull
	@NotEmpty
	@ListEmailConstraint
	private List<String> emailTo;

	@ListEmailConstraint
	private List<String> emailCarbonCopies;

	@ListEmailConstraint
	private List<String> blindCarbonCopies;

	@NotEmpty
	@NotNull
	private String subject;

	@NotEmpty
	@NotNull
	private String body;

	@Email
	private String emailFrom;


}
