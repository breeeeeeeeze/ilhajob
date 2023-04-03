package com.itwill.ilhajob.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itwill.ilhajob.app.App;
import com.itwill.ilhajob.corpimage.CorpImage;
import com.itwill.ilhajob.manager.Manager;
import com.itwill.ilhajob.order.Orders;
import com.itwill.ilhajob.recruit.Recruit;
import com.itwill.ilhajob.review.Review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "corp")
public class Corp {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "corp_id_SEQ_gen")
	private Long id;
	
	private String corpPassword;
	
	private String corpName;
	
	private String corpPhone;
	
	private String corpBusinessNo;
	
	private String corpWebsite;
	
	private Date corpEst;
	
	private String corpSize;
	
	private String corpSales;
	
	private String corpComment;
	
	private String corpWelfare;
	
	private String corpAddress;
	
	private char corpStatus;
	
	private String job;
	
	private int role;
	
	@OneToMany(mappedBy = "corp",
			   cascade = CascadeType.PERSIST,
			   fetch = FetchType.EAGER)
	private List<App> appList = new ArrayList<App>();
	
	@OneToMany(mappedBy = "corp",
			   fetch = FetchType.EAGER)
	private List<Orders> orders = new ArrayList<Orders>();
	
	@OneToMany(mappedBy = "corp",
			   cascade = CascadeType.PERSIST,
			   fetch = FetchType.EAGER)
	private List<Recruit> recruitList = new ArrayList<Recruit>();
	
	@OneToMany(mappedBy = "corp",
			   cascade = CascadeType.PERSIST,
			   fetch = FetchType.EAGER)
	private List<Review> reviewList = new ArrayList<Review>();
	
	@OneToMany(mappedBy = "corp",
			   cascade = CascadeType.PERSIST,
			   fetch = FetchType.EAGER)
	private List<CorpImage> corpImageList = new ArrayList<CorpImage>();
	
	@OneToMany(mappedBy = "corp",
			   cascade = CascadeType.PERSIST,
			   fetch = FetchType.EAGER)
	private List<Manager> managerList = new ArrayList<Manager>();
	
	@OneToMany(mappedBy = "corp",
               fetch = FetchType.EAGER)
	private List<CorpTag> corpTagList = new ArrayList<CorpTag>();
	
	@OneToMany(mappedBy = "corp",
			   cascade = CascadeType.PERSIST,
			   fetch = FetchType.EAGER)
	private List<CorpBookmark> corpBookmarkList = new ArrayList<CorpBookmark>();
	
	@OneToMany(mappedBy = "corp",
			   cascade = CascadeType.PERSIST,
			   fetch = FetchType.EAGER)
	private List<Payment> paymentList = new ArrayList<Payment>();
}

