package uk.codingbadgers.badmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanEntry {

	private String name;
	private BanType type;
	private String reason;
	
}
