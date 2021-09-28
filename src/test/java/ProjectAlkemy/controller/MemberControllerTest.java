package ProjectAlkemy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ProjectAlkemy.controller.dto.MemberDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.MemberRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Member;
import ProjectAlkemy.service.MemberService;

class MemberControllerTest {
	
	private MemberService memberService;
	private MemberController memberController;
	private Member member;
	private MemberDto memberDto;
	private MemberRequest memberRequest,memberRequest2;
	private Map<String,Object> map;
	
	

    @BeforeEach
    void setUp() {
    	
    	memberService = Mockito.mock(MemberService.class);
    	memberController = new MemberController(memberService);
    	member = new Member(1L,"member","facebook","instagram","linkedin","image","description",false);
    	memberDto = MemberDto.entityToDto(member);
    	
    	memberRequest2 = new MemberRequest();
    	memberRequest= new MemberRequest("member","facebook","instagram","linkedin","image","description");
    	
    	
    }

    @Test
    void createMemberOk() {
    	MockedStatic<MemberRequest> mb = Mockito.mockStatic(MemberRequest.class);
		mb.when(() -> { MemberRequest.mapToEntity(memberRequest); }).thenReturn(member);
    	when(memberService.create(member)).thenReturn(member);
    	ResponseEntity<?> ok = memberController.createMember(memberRequest);
    	assertEquals(ok,new ResponseEntity<>(new ResponseDto(200,"id :"+member.getId()),HttpStatus.OK));
    	verify(memberService,times(1)).create(member);
   
    }
    @Test
    void createMemberBadRequest() {
    	
    	ResponseEntity<?> badRequest = memberController.createMember(memberRequest2);
    	assertEquals(badRequest,new ResponseEntity<>(new ResponseDto(400,"Name and image are required"),HttpStatus.BAD_REQUEST));
    }

    @Test
    void memberUpdateOk() throws RecordNotExistException {
    	when(memberService.setMember(memberRequest, 1L)).thenReturn(member);
    	when(memberService.update(member)).thenReturn(member);
    	ResponseEntity<?> ok= memberController.MemberUpdate(1L, memberRequest);
    	assertEquals(ok,new ResponseEntity<>(memberDto, HttpStatus.OK));
    	verify(memberService,times(1)).setMember(memberRequest, 1L);
    	verify(memberService,times(1)).update(member);

    }
    @Test
    void memberUpdateException() throws RecordNotExistException {

            when(memberService.getById(2l)).thenThrow(new RecordNotExistException("not Found"));

            assertThrows(RecordNotExistException.class, () -> memberService.getById(2l));
            verify(memberService,times(1)).getById(2l);
    }

    @Test
    void getMemberByIdOk() throws RecordNotExistException {
    	when(memberService.getById(1l)).thenReturn(member);
        ResponseEntity<?> ok= memberController.getMemberById(1l);

        assertEquals(ok,new ResponseEntity<>(memberDto, HttpStatus.OK));
        verify(memberService,times(1)).getById(1l);
    	
    } 
    @Test
    void getMemberByIdException() throws RecordNotExistException {

        when(memberService.getById(2l)).thenThrow(new RecordNotExistException("not Found"));

        assertThrows(RecordNotExistException.class, () -> memberService.getById(2l));
        verify(memberService,times(1)).getById(2l);
    	
    }
    @Test
    void deleteMemberOk() throws RecordNotExistException {
    	when(memberService.getById(1L)).thenReturn(member);
    	memberService.deleteMember(member);
    	ResponseEntity<?> ok = memberController.deleteMember(1L);
    	assertEquals(ok,new ResponseEntity<>(new ResponseDto(200,"has been successfully deleted."),HttpStatus.OK));
    	  verify(memberService,times(1)).getById(1l);
    }
    @Test
    void deleteMemberException()throws RecordNotExistException {

        when(memberService.getById(2l)).thenThrow(new RecordNotExistException("not Found"));

        assertThrows(RecordNotExistException.class, () -> memberService.getById(2l));
        verify(memberService,times(1)).getById(2l);
    }
    @Test
    void getAllMemberOk() throws ListNotFoundException {
    	
    	when(memberService.getAllMemberPaged(0, 10)).thenReturn(map);
    	ResponseEntity<?> ok = memberController.getAllMember(0, 10);
    	assertEquals(ok,new ResponseEntity<>(map,HttpStatus.OK));
    	verify(memberService,times(1)).getAllMemberPaged(0, 10);
    	
    }
    @Test
    void getAllMemberException() throws ListNotFoundException {
    	  when(memberService.getAllMemberPaged(0, 10)).thenThrow(new ListNotFoundException("not Found"));

          assertThrows(ListNotFoundException.class, () -> memberService.getAllMemberPaged(0, 10));
          verify(memberService,times(1)).getAllMemberPaged(0, 10);
    	
    }
    
}