package org.apac.erp.cach.forecast.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class AuditableSql {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	@Column(nullable = false, updatable = false)
	@CreatedDate
	protected Date createdAt;

	/** The updated at. */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Tunis")
	@Column(nullable = false)
	@LastModifiedDate
	protected Date updatedAt;

	@PrePersist
	private void persistCreatedDate() {
		if (this.createdAt == null) {
			this.createdAt = new Date();
		}
		this.updatedAt = new Date();
	}

	@PreUpdate
	private void persistUpdatedDate() {
		this.updatedAt = new Date();
	}

}
