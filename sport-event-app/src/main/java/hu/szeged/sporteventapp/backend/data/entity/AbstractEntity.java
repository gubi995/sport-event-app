package hu.szeged.sporteventapp.backend.data.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import hu.szeged.sporteventapp.common.util.IdGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class AbstractEntity implements Serializable {

	@Id
	@Column(name = "id", nullable = false, unique = true, updatable = false, length = 32)
	protected String id = IdGenerator.generateId();

	@Version
	protected Long version;

	@Column(name = "created_at", nullable = false)
	protected LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	protected LocalDateTime updatedAt;

	@PreUpdate
	protected void preUpdate() {
		setUpdatedAt(LocalDateTime.now());
	}

	@PrePersist
	protected void prePersist() {
		setCreatedAt(LocalDateTime.now());
		preUpdate();
	}

	@Transient
	public boolean isNew() {
		return Objects.isNull(version);
	}

}
