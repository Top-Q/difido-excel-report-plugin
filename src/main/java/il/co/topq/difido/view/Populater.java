package il.co.topq.difido.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import il.co.topq.difido.model.Table;

public interface Populater {

	File populate(List<Table> tables) throws IOException;

}
