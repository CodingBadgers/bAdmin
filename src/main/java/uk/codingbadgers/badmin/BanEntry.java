package uk.codingbadgers.badmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BanEntry {

	private final String name;
	private final BanType type;
	private final String reason;
	private String data = "";
	
}
