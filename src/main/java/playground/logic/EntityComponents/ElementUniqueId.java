package playground.logic.EntityComponents;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ElementUniqueId implements Serializable {

	private static final long serialVersionUID = 4294359810914067582L;
	 
	private static final String DEL = "$$";
	private static final String DEF_PLAYGROUND = "2019A.yuri";
	
	@Column(name="id")
	private String	id;
	
	@Column(name="playground")
	private String	playground;

	
	public ElementUniqueId() {
		
	}
	
	public ElementUniqueId(String id, String playground) {
		this.id = id;
		this.playground = playground;
	}
	
	public ElementUniqueId(String id) {
		this.id = id;
		this.playground = DEF_PLAYGROUND;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPlayground() {
		return this.playground;
	}
	
	public void setPlayground(String playground) {
		this.playground = playground;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ElementUniqueId)) {
			return false;
		}
		ElementUniqueId element = (ElementUniqueId) o;
		return Objects.equals(this.id, element.getId()) && Objects.equals(this.playground, element.getPlayground());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.playground);
	}
	
	@Override
	public String toString() {
		return playground + DEL + id;
	}
}
