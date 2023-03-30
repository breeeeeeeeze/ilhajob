package com.itwill.ilhajob.cv;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itwill.ilhajob.cv.mapper.CvMapper;

@Repository
public class CvDaoImpl_하은 implements CvDao_하은{

	@Autowired
	private CvMapper cvMapper;

	@Override
	public Cv findCv(int cvSeq) {
		return cvMapper.selectByCv(cvSeq);
	}

	@Override
	public List<Cv> findCvList() {
		return cvMapper.selectAll();
	}
/*
	@Override
	public List<Cv> findCvOfUser(String userEmail) {
		return cvMapper.selectByCvOfUserEmail(userEmail);
	}
*/
	@Override
	public List<Cv> findCvListByUserSeq(int userSeq) {
		List<Cv> cvList = new ArrayList<Cv>();
		
		return cvMapper.selectByCvOfUserSeq(userSeq);
	}
	
	@Override
	public int createCv(Cv cv) {
		return cvMapper.insertCv(cv);
	}

	@Override
	public int updateCv(Cv cv) {
		return cvMapper.updateCv(cv);
	}

	@Override
	public int remove(int cvSeq) {
		return cvMapper.deleteCv(cvSeq);
	}

	@Override
	public Cv detailCv(int userSeq, int cvSeq) {
		return cvMapper.selectDetailCv(userSeq,cvSeq);
	}

	
}
