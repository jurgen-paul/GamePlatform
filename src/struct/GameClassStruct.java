package struct;

public class GameClassStruct {
	private int id;
	private String name;
	private String link;
	
	public GameClassStruct(int id, String name, String link) {
		this.id = id;
		this.name = name;
		this.link = link;
	}

	public GameClassStruct(String s){
		split(s);
	}
	public GameClassStruct(){
		
	}
	
	public boolean split(String s) {
		s = s.trim();
		String[] strings = s.split(",");
		if(strings.length != 3){
			return false;
		}
		this.id = Integer.parseInt(strings[0].trim());
		this.name = strings[1].trim();
		this.link = strings[2].trim();
		
		return true;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
