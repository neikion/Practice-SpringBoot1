package com.p1.kr.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.p1.kr.domain.DomainBoardContent;
import com.p1.kr.domain.DomainBoardFile;
import com.p1.kr.domain.DomainBoardList;
import com.p1.kr.mybatis.IMapperUpload;
import com.p1.kr.util.CommonUtils;
import com.p1.kr.vo.VOFileList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ServiceUpload implements IServiceUpload{

	@Autowired
	private IMapperUpload mapper;
	
	@Override
	public List<DomainBoardList> listboard() {
		return mapper.listboard();
	}

	@Override
	public int fileProcess(VOFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpreq) {
		HttpSession session = httpreq.getSession();
		
		//content domain 생성 
		DomainBoardContent contentdomain = DomainBoardContent.builder()
				.id(session.getAttribute("id").toString())
				.title(vo.getTitle())
				.content(vo.getContent())
				.build();
		
				if(vo.getIsEdit() != null) {
					contentdomain.setSeq(Integer.parseInt(vo.getSeq()));
					// db udpate
					mapper.updateContent(contentdomain);
				}else {	
					// db insert
					mapper.uploadContent(contentdomain);
				}
				
				// file 데이터 db 저장시 쓰일 값 추출
				int seq = contentdomain.getSeq();
				String id = contentdomain.getId();
				
				//파일객체 담음
				List<MultipartFile> filelist = request.getFiles("files");
				
				
				// 게시글 수정시 파일관련 물리저장 파일, db 데이터 삭제 
				if(vo.getIsEdit() != null) { // 수정시 

	
					List<DomainBoardFile> fileList = null;
					
					
					
					for (MultipartFile file : filelist) {
					
					
						if(!file.isEmpty()) {   // 수정시 새로 파일 첨부될때 세션에 담긴 파일 지우기
							
							
							if(session.getAttribute("files") != null) {	

								fileList = (List<DomainBoardFile>) session.getAttribute("files");
								
								for (DomainBoardFile list : fileList) {
									list.getPath();
									Path filePath = Paths.get(list.getPath());
							 
							        try {
							        	
							            // 파일 삭제
							        	// notfound시 exception 발생안하고 false 처리
							            Files.deleteIfExists(filePath); 
							            //삭제 
							            removeFile(list); //데이터 삭제
										
							        }catch (Exception e) {
							            e.printStackTrace();
							        }
								}
								
							}
							
						}

					}
					
					
				}
				
				
				///////////////////////////// 새로운 파일 저장 ///////////////////////
				
				// 저장 root 경로만들기
				Path rootpath = Paths.get(new File("C://").toString(),"upload", File.separator).toAbsolutePath().normalize();			
				File pathCheck = new File(rootpath.toString());
				
				// folder chcek
				if(!pathCheck.exists()) pathCheck.mkdirs();
				
	
				for (MultipartFile mfile : filelist) {
					
					if(!mfile.isEmpty()) {  // 파일 있을때 
						
						//확장자 추출
						String FileExtension;
						String contentType = mfile.getContentType();
						String origianlName = mfile.getOriginalFilename();
						
						//확장자 존재하지 않을 경우
						if(ObjectUtils.isEmpty(contentType)){
							break;
						}else {
							if(contentType.contains("image/jpeg")) {
								FileExtension = ".jpg";
							}else if(contentType.contains("image/png")) {
								FileExtension = ".png";
							}else {
								break;
							}
						}
						
						//파일명을 업로드한 날짜로 변환하여 저장
						String uuid = UUID.randomUUID().toString();
						String currenttime = CommonUtils.currentTime();
						String newName = uuid + currenttime + FileExtension;
						//최종경로까지 지정
						Path targetPath = rootpath.resolve(newName);
						
						File newfile = new File(targetPath.toString());
						
						try {
							//파일복사저장
							mfile.transferTo(newfile);
							// 파일 권한 설정(쓰기, 읽기)
							newfile.setWritable(true);
							newfile.setReadable(true);

							DomainBoardFile filedomain = DomainBoardFile.builder()
									.seq(seq)
									.id(id)
									.originalname(origianlName)
									.newname("resources/upload/"+newName) // WebConfig에 동적 이미지 폴더 생성 했기때문
									.path(targetPath.toString())
									.filesize((int)mfile.getSize())
									.build();
								mapper.uploadFile(filedomain);
						} catch (Exception e) {
//							throw RequestException.fire(Code.E404, "잘못된 업로드 파일", HttpStatus.NOT_FOUND);
							e.printStackTrace();
						}
					}

				}
				return seq; // 저장된 게시판 번호
	}

	@Override
	public void removeContent(HashMap<String, Object> map) {
		mapper.removeContent(map);
	}

	@Override
	public void removeFile(DomainBoardFile domain) {
		mapper.removeFile(domain);
	}

	@Override
	public DomainBoardList selectBoard(HashMap<String, Object> map) {
		return mapper.selectBoard(map);
	}

	@Override
	public List<DomainBoardFile> selectBoardFile(HashMap<String, Object> map) {
		return mapper.selectBoardFile(map);
	}
	
}
