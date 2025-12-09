package de.hsh.dbs2.imdb;

import javax.swing.SwingUtilities;

import de.hsh.dbs2.imdb.gui.SearchMovieDialog;
import de.hsh.dbs2.imdb.gui.SearchMovieDialogCallback;

public class Starter {

	/**
	 * @param args command line arguments (none)4
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Starter().run());
	}
	
	public void run() {
        SearchMovieDialogCallback callback = new SearchMovieDialogCallback();
		SearchMovieDialog sd = new SearchMovieDialog(callback);
		sd.setVisible(true);
	}
}
