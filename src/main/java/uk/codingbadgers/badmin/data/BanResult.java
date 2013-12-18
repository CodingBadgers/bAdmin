package uk.codingbadgers.badmin.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(staticName = "notBanned")
@AllArgsConstructor(staticName = "create")
public class BanResult {

	private boolean banned = false;
	private DataEntry entry = null;
	
}
