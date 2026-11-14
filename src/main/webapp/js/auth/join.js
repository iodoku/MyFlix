$(function(){
	/*** [dom 요소] ***/
	/***1. 아이디 ***/
	const ipId       = $("#userid");
	const idCheckBtn = $("#idCheckBtn");
	const idCheckMsg = $("#idMsg"); 
	/***2. 비밀번호  ***/
	const ipPw          = $("#userpw");
	const viewPw1Btn    = $("#viewpw1");
	const pwCheckMsg    = $("#pwMsg");
	const userpwConfirm = $("#userpwConfirm");
	const viewPw2Btn    = $("#viewpw2");
	const pwConfirmMsg  = $("#pwConfirmMsg");
	/***3. 이메일 ***/
	const ipEmailId         = $("#emailid");
	const ipEmailDomain     = $("#emaildomain");
	const emailDomainSelect = $("#emailDomainSelect");
	const sendEmailCode     = $("#sendEmailCode");
	const ipEmailAuthCode   = $("#emailcode");
	const verifyEmailCode   = $("#verifyEmailCode");
	/***4. 닉네임 ***/
	const ipNick       = $("#nick");
	const nickCheckBtn = $("#nickCheckBtn");
	const nickCheckMsg = $("#nickMsg");
	/***5. 취소버튼 ***/
	const cancelBtn    = $("#cancelBtn");
	/***6. 가입Form ***/
	const joinForm     = $("#joinForm");
	
	/*** [정규식] ***/
	const idRegex = /^[a-z0-9]{5,10}$/;
	//영어소문자, 숫자 포함 5~10자
	const pwRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!?#&*])[a-zA-Z\d!?#&*]{6,15}$/;
	//영문자, 숫자, 특수문자(!,?,#,&,*) 각 1개이상 포함 6~15자
	const nickRegex = /^[a-zA-Z0-9가-힣_]{3,20}$/;
	//공백 불가, 특수문자(_)만 가능, 3~10자
	
	/*** [상태 플래그 변수] ***/
	let isIdChecked     = false;
	let isNickChecked   = false;
	let isEmailVerified = false;
	
	
	/*** [Function] ***/
	/*** 1. ipid 유효성 검사 ***/
	function idVal()
	{
		const id = ipId.val().trim();
		idCheckMsg.removeClass("msg-error msg-info");
		if(id === "")
		{
			idCheckMsg.text("아이디를 입력하세요").addClass("msg-error");
			return false;
		}
		
		if(!idRegex.test(id))
		{
			idCheckMsg.text("영어소문자, 숫자 포함 5~10자").addClass("msg-error");
			return false;
		}
		
		idCheckMsg.text("아이디중복검사를 진행해주세요").addClass("msg-info");
		return true;
	}
	
	/***2. ippw 유효성 검사 ***/
	function pwVal() 
	{
	    const pw = ipPw.val().trim();
	    pwCheckMsg.removeClass("msg-error msg-info");
	    if(pw === "") 
	    {
	        pwCheckMsg.text("비밀번호를 입력하세요").addClass("msg-error");
	        return false;
	    }
	    if(!pwRegex.test(pw))
	    {
	        pwCheckMsg.text("영문자, 숫자, 특수문자(!,?,#,&,*) 각 1개이상 포함 6~15자").addClass("msg-error");
	        return false;
	    }
	    pwCheckMsg.text("사용 가능한 비밀번호입니다").addClass("msg-info");
	    return true;
	}
	
	/***3. pwView 기능 */
	function pwView($inputTarget)
	{
		let currentType = $inputTarget.attr("type");
		$inputTarget.attr("type", currentType === "password" ? "text" : "password");
	}
	
	/***4. ippw 일차, 이차 비교 */
	function pwMatch()
	{
		const pw1 = ipPw.val().trim();
		const pw2 = userpwConfirm.val().trim();
		pwConfirmMsg.removeClass("msg-error msg-info");
		if(pw2 === "")
		{
			pwConfirmMsg.text("비밀번호 확인을 입력해주세요").addClass("msg-error");
			return false;			
		}
		if(pw1 === pw2)
		{
			pwConfirmMsg.text("비밀번호가 일치합니다").addClass("msg-info");
			return true;
		}else{
			pwConfirmMsg.text("비밀번호가 일치하지 않습니다").addClass("msg-error");
			return false;
		}
	}
	
	/***5. ipnick 유효성 검사 ***/
	function nickVal() {
	    const nick = ipNick.val().trim();
	    nickCheckMsg.removeClass("msg-error msg-info");
	    
	    if(nick === "") 
	    {
	        nickCheckMsg.text("닉네임을 입력하세요").addClass("msg-error");
	        return false;
	    }
	    if(!nickRegex.test(nick)) 
	    {
	        nickCheckMsg.text("공백 불가, 특수문자(_)만 가능, 3~20자").addClass("msg-error");
	        return false;
	    }
	    nickCheckMsg.text("닉네임 중복검사를 진행해주세요").addClass("msg-info");
	    return true;
	}
	
	/*** [UI, UX] ***/
	/***1. 페이지 최초 로드시***/
	ipId.focus();
	
	/***2. id 관련***/
	ipId.on("input", function(){
		isIdChecked = false;
		idCheckMsg.text("").removeClass("msg-error msg-success msg-info");
	});
	
	ipId.on("blur", idVal);
	
	idCheckBtn.on("click", function(){
		if(!idVal())
		{
			ipId.focus();
			return;
		}
		const id = ipId.val().trim();
		$.ajax({
			url : contextPath + "/joinidcheck",
			type : "POST",
			dataType : "json",
			data : {userid : id},
			success : function(result){
				idCheckMsg.removeClass("msg-error msg-success msg-info");
				if(result.msg === "OK"){
					idCheckMsg.text("사용가능한 아이디입니다").addClass("msg-success");
					isIdChecked = true;
				}else{
					idCheckMsg.text("이미 사용중인 아이디입니다").addClass("msg-error");
					isIdChecked = false;
				}
			},
			error : function(err){
				console.error(err);
				alert("에러 발생");
			}
		})
	});
	
	/***3. pw 관련 ***/
	/***3-1. 1차 ippw 관련 ***/
	ipPw.on("input", function(){
		pwCheckMsg.text("").removeClass("msg-error msg-info");
		if(userpwConfirm.val().trim() !== "") 
		{
			pwMatch();
		}
	});
	
	ipPw.on("blur", pwVal);

	viewPw1Btn.on("click",function(){
		pwView(ipPw);
	});

	/***3-2. 2차 ippw 관련 ***/	
	userpwConfirm.on("input", function(){
		pwConfirmMsg.text("").removeClass("msg-error msg-info");
	});
	
	userpwConfirm.on("blur", pwMatch);
	
	viewPw2Btn.on("click",function(){
		pwView(userpwConfirm);
	});
	
	/***4. ipnick 관련 ***/
	ipNick.on("input", function(){
		isNickChecked = false;
		nickCheckMsg.text("").removeClass("msg-error msg-info");
	});
	
	ipNick.on("blur", nickVal);
	
	nickCheckBtn.on("click", function(){
		if(!nickVal())
		{
			ipNick.focus();
			return;
		}
		const nick = ipNick.val().trim();
		$.ajax({
			url : contextPath + "/joinnickcheck",
			type : "POST",
			dataType : "json",
			data : {nick : nick},
			success : function(result){
				nickCheckMsg.removeClass("msg-error msg-info");
				if(result.msg === "OK"){
					nickCheckMsg.text("사용가능한 닉네임입니다").addClass("msg-info");
					isNickChecked = true;
				}else{
					nickCheckMsg.text("이미 사용중인 닉네임입니다").addClass("msg-error");
					isNickChecked = false;
				}
			},
			error : function(err){
				console.error(err);
				alert("에러 발생");
			}
		})
	});
	
	/***5. email 관련 ***/
	/***5-1. ipemail 초기화 및 emailselect 관련*/
	ipEmailId.on("input", function() {
		isEmailVerified = false;
	});
	
	ipEmailDomain.on("input", function() {
		isEmailVerified = false;
	});	
	
	emailDomainSelect.on("change", function(){
		const selectVal = $(this).val();
		if(selectVal === "")
		{
			ipEmailDomain.val("").prop("readonly", false).focus();
		}else{
			ipEmailDomain.val(selectVal).prop("readonly", true);
		}
	});
	
	/***5-2 이메일 인증번호 발송 관련 ***/
	isSendEmail = false;  //메일 발송중인가?
	
	sendEmailCode.on("click", function() {
		if(isSendEmail == true)
		{
			alert("인증코드가 발송중입니다. 기다려주세요..");
			return;
		}
	    console.log("인증번호 발송 버튼 클릭됨");
	
	    // 이메일 아이디
	    let emailId = ipEmailId.val().trim();
	    // 이메일 도메인
	    let emailDomain = ipEmailDomain.val().trim();
	    // 이메일 입력 확인
	    if (emailId == "" || emailDomain == "") 
	    {
	        alert("이메일을 입력해주세요.");
	        ipEmailId.focus();
	        return;
	    }
	
		//$("#sendMailMsg").html("인증코드 발송중입니다. 잠시만 기다려 주세요..");
		isSendEmail = true;
	    $.ajax({
	        url: contextPath + "/sendemail",
	        type: "POST",
	        data: 
	        {
	            emailid : emailId,
	            emaildomain : emailDomain,
	            emailtype : "회원가입"
	        },
	        dataType: "json",
	        success: function(result) {
				isSendEmail = false;
	            if (result.msg == "OK") 
	            {
	                alert("인증번호가 발송되었습니다.");
	                //$("#sendMailMsg").html("인증번호가 발송되었습니다.");
	            } else {
	                alert("인증번호 발송에 실패했습니다.");
	                //$("#sendMailMsg").html("인증번호가 발송되었습니다.");
	            }
	        },
	        error: function(xhr, status, error) {
	            console.log(xhr.responseText);
	            console.log(status);
	            console.log(error);
	            alert("서버 오류가 발생했습니다.");
	        }
	    });
	
	});
	
	/***5-3 이메일 인증번호 확인 ***/
	verifyEmailCode.on("click", function(){
		let code = ipEmailAuthCode.val().trim();
		let emailId = ipEmailId.val().trim();
		let emailDomain = ipEmailDomain.val().trim();
		let email = emailId + "@" + emailDomain;
		if(code === "") 
		{
			alert("인증번호를 입력해주세요.");
			ipEmailAuthCode.focus();
			return;
		}

		$.ajax({
			url: contextPath + "/codecheck",
			credentials: 'include',				//세션 정보 전달하기 위해 사용
			type: "POST",
			data: {
				emailcode: code,
				emailtype : "회원가입"
			},
			dataType: "json",
			success: function(result) {
				if(result.msg === "OK") {
					alert("이메일 인증이 완료되었습니다.");
					isEmailVerified = true;
					// 인증 완료 후 이메일 수정 방지
					ipEmailId.prop("readonly", true);
					ipEmailDomain.prop("readonly", true);
					emailDomainSelect.prop("disabled", true);
					ipEmailAuthCode.prop("readonly", true);
				} else {
					alert("인증번호가 일치하지 않거나 만료되었습니다.");
					isEmailVerified = false;
				}
			},
			error: function(xhr, status, error) {
				console.error(xhr.responseText);
				alert("인증 확인 중 오류가 발생했습니다.");
			}
		});
	});
	/*** [취소 버튼] ***/
	cancelBtn.on("click", function(e){
		e.preventDefault();
		history.back();
	});
	
	/*** [가입클릭시] 최종 유효성 검사 ***/	
	joinForm.on("submit", function(e){
		if(!idVal()) 
		{
			e.preventDefault();
			ipId.focus();
			return;
		}
		if(!isIdChecked) 
		{
			e.preventDefault();
			alert("아이디 중복확인을 진행해주세요.");
			ipId.focus();
			return;
		}
		if(!pwVal()) 
		{
			e.preventDefault();
			ipPw.focus();
			return;
		}
		if(!pwMatch()) 
		{
			e.preventDefault();
			userpwConfirm.focus();
			return;
		}
		if(!isEmailVerified) {
			e.preventDefault();
			alert("이메일 인증을 완료해주세요.");
			ipEmailAuthCode.focus();
			return;
		}		
		if(!nickVal()) 
		{
			e.preventDefault();
			ipNick.focus();
			return;
		}
		if(!isNickChecked) 
		{
			e.preventDefault();
			alert("닉네임 중복확인을 진행해주세요.");
			ipNick.focus();
			return;
		}
	});
})



