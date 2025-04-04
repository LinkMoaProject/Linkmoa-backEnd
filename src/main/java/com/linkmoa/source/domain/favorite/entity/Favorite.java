package com.linkmoa.source.domain.favorite.entity;

import com.linkmoa.source.domain.favorite.constant.ItemType;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorite")
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favorite_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "item_id", nullable = false)
	private Long itemId;

	@Enumerated(EnumType.STRING)
	@Column(name = "item_type", nullable = false)
	private ItemType itemType;

	@Column(name = "order_index")
	private Integer orderIndex;

	@Builder
	private Favorite(Member member, Long itemId, ItemType itemType, Integer orderIndex) {
		this.member = member;
		this.itemId = itemId;
		this.itemType = itemType;
		this.orderIndex = orderIndex;
	}

}
