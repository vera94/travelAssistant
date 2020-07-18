package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class LandmarkType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String type;
	private String path;
	private String gmapMapping;

	@Transient
	private String parentPath;

	@Transient
	private List<LandmarkType> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGmapMapping() {
		return gmapMapping;
	}

	public void setGmapMapping(String gmapMapping) {
		this.gmapMapping = gmapMapping;
	}

	public List<LandmarkType> getChildren() {
		return children;
	}

	public void setChildren(List<LandmarkType> children) {
		this.children = children;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
}