package com.kh.walkwork.common.model.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.walkwork.common.model.dao.AttachmentDao;
import com.kh.walkwork.common.model.vo.Attachment;

@Service
public class AttachmentService {

	@Autowired
	private AttachmentDao attachmentDao;

	public int insertAttachment(Attachment a) {
		return attachmentDao.insertAttachment(a);
	}

	public List<Attachment> selectAttachmentList(Attachment a) {
		return attachmentDao.selectAttachmentList(a);
	}
}