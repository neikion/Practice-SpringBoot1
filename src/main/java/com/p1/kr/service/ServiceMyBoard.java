package com.p1.kr.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.p1.kr.domain.DomainMyBoard;
import com.p1.kr.domain.DomainMyBoardFile;
import com.p1.kr.domain.DomainMyBoardList;
import com.p1.kr.mybatis.IMapperMyUpload;
import com.p1.kr.util.CommonUtils;
import com.p1.kr.vo.VOMyFileList;

@Service
@Transactional
public class ServiceMyBoard implements IServiceMyBoard {

	@Autowired
	private IMapperMyUpload mapper;
	@Override
	public List<DomainMyBoardList> getBoardList() {
		List<DomainMyBoardList> boardlist=mapper.getBoardList();
		DomainMyBoardFile file;
		for(DomainMyBoardList board:boardlist) {
			file=mapper.getFile(board.getBoardid());
			if(file!=null) {
				board.setNewname(file.getNewname());
			}
		}
		return boardlist;
	}
	
	@Override
	public int uploadFile(VOMyFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpreq) {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		DomainMyBoard board = DomainMyBoard.builder()
				.author(session.getAttribute("id").toString())
				.title(vo.getTitle())
				.content(vo.getContent())
				.build();
		List<MultipartFile> files=request.getFiles("files");
		//System.getenv("SystemDrive")
		Path savepath=Paths.get("C://","myupload",File.separator).toAbsolutePath().normalize();
		File pathCheck=new File(savepath.toString());
		if(!pathCheck.exists()) {
			pathCheck.mkdir();
		}
		if(vo.getIsEdit()!=null) {
			board.setBoardid(Integer.parseInt(vo.getBoardid()));
			mapper.updateContent(board);
			removeFiles(board.getBoardid());
		}else {
			mapper.uploadContent(board);
		}
		if(!files.isEmpty()) {
			String fileExtension;
			String filetype;
			String filename;
			for(MultipartFile file:files) {
				if(files.isEmpty()) {
					continue;
				}
				filetype=file.getContentType();
				filename=file.getOriginalFilename();
				if(ObjectUtils.isEmpty(filetype)) {
					continue;
				}
				if(filetype.contains("image/jpeg")) {
					fileExtension=".jpg";
				}else if(filetype.contains("image/png")) {
					fileExtension = ".png";
				}else {
					continue;
				}
				String randomid = UUID.randomUUID().toString();
				String currenttime = CommonUtils.currentTime();
				String newName = randomid + currenttime + fileExtension;
				Path targetPath = savepath.resolve(newName);
				File newfile= new File(targetPath.toString());
				try {
					newfile.setWritable(true);
					newfile.setReadable(true);
					file.transferTo(newfile);
					
					DomainMyBoardFile domainfile=DomainMyBoardFile.builder()
							.boardid(board.getBoardid())
							.originname(filename)
							.newname("resources/myupload/"+newName)
							.path(targetPath.toString())
							.filesize((int)file.getSize())
							.build();
					mapper.uploadFile(domainfile);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		return board.getBoardid();
	}

	@Override
	public void removeContent(int id) {
		// TODO Auto-generated method stub
		mapper.removeContent(id);
	}

	@Override
	public DomainMyBoard getBoard(int boardid) {
		return mapper.getBoard(boardid);
	}

	@Override
	public List<DomainMyBoardFile> getFileList(int boardid) {
		List<DomainMyBoardFile> result=mapper.getFileList(boardid);
		for (DomainMyBoardFile list : result) {
			String path = list.getPath().replaceAll("\\\\", "/");
			list.setPath(path);
		}
		return result;
	}

	@Override
	public void removeFile(int id) {
		mapper.removeFile(id);
		
	}

	
	@Override
	public void removeFiles(int id) {
		List<DomainMyBoardFile> dbfile= mapper.getFileList(id);
		Path removepath;
		try {
			for(DomainMyBoardFile file:dbfile) {
				removepath=Paths.get(file.getPath());
				Files.deleteIfExists(removepath);
				removeFile(file.getBoardid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public DomainMyBoardFile getFile(int boardid) {
		return mapper.getFile(boardid);
	}

	
}
