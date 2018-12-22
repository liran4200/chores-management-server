package playground.plugins;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistoryBoard {
	private List<String> history;
	
	public ActivityHistoryBoard() {
		history = new ArrayList<>();
	}
	
	public ActivityHistoryBoard(List<String> history) {
		this.history = history;
	}
	
	public void addHistoryRecored(String hr) {
		this.history.add(hr);
	}

	public List<String> getHistory() {
		return history;
	}

	public void setHistory(List<String> history) {
		this.history = history;
	}
}
