package filter.model;

public class Task {
	private String name;
	
	public Task(){
	}
	
	public Task(String taskString){
		this.name = taskString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
