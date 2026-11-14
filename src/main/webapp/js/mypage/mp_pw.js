/* 비밀번호 보기, 숨기기 */
	function togglePasswordVisibility(buttonBtn) 
	{
	    const button = $(buttonBtn);
	    const input = button.prev("input");
	    if (input.attr("type") === "password") 
	    {
	        input.attr("type", "text");
	        button.text("숨기기");
	        
	    } 
	    else 
	    {
	        input.attr("type", "password");
	        button.text("보기");
	    }
	}

$(function() {
	/* [비밀번호 정규식] */
	// 영문, 숫자, 특수문자 최소 1개이상 포함 6~15자 - 회원가입 기준
	const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!?#&*])[A-Za-z\d!?#&*]{6,15}$/;
	
	/* 선택자 변수 */
	const currentPw = $("#currentPw"); // 현재 비밀번호
	const newPw = $("#newPw"); // 새 비밀번호
	const newPwConfirm = $("#newPwConfirm"); // 새 비밀번호 재입력
	const pwSubmitBtn = $("#pwSubmitBtn"); // 변경하기 버튼
	
	/* 각 메시지 */
	const newPwCheckMsg = $("#newPwCheckMsg"); // 새 비밀번호 정규식 만족여부 메시지
	const pwConfirmMsg = $("#pwConfirmMsg"); // 재입력시 일치여부 메시지
	
	/* 현재 비밀번호 입력시 유효성 검사 */
	function currentPwVal() {
		const curPw = currentPw.val().trim();
		if (curPw === "") {
			alert("현재 비밀번호를 입력해주세요");
			currentPw.focus();
			return false;
		}
		return true;
	}
	
	/* 새 비밀번호 입력시 유효성 검사 */
	function newPwVal() {
		const pw = newPw.val().trim();
		newPwCheckMsg.removeClass("msg-error msg-info msg-success");
		
		if (pw === "") {
			newPwCheckMsg.text("새 비밀번호를 입력하세요").addClass("msg-error");
			return false;
		}
		if (!pwRegex.test(pw)) {
			newPwCheckMsg.text("영문자, 숫자, 특수문자(!,?,#,&,*) 각 1개이상 포함 6~15자").addClass("msg-error");
			return false;
		}
		newPwCheckMsg.text("사용 가능한 비밀번호입니다").addClass("msg-success");
		return true;
	}
	
	/* 비밀번호 재입력시 대조 */
	function pwMatch() {
		const pw1 = newPw.val().trim();
		const pw2 = newPwConfirm.val().trim();
		pwConfirmMsg.removeClass("msg-error msg-info msg-success");
		
		if (pw2 === "") {
			pwConfirmMsg.text("비밀번호 확인을 입력해주세요").addClass("msg-error");
			return false;
		}
		if (pw1 === pw2) {
			pwConfirmMsg.text("비밀번호가 일치합니다").addClass("msg-success"); // ✓ 
			return true;
		} else {
			pwConfirmMsg.text("비밀번호가 일치하지 않습니다").addClass("msg-error");
			return false;
		}
	}
	
	/* 페이지 최초 입장 시 */
	currentPw.focus();
	
	/* 새 비밀번호 입력창 */
	newPw.on("input", function() {
		newPwCheckMsg.text("").removeClass("msg-error msg-info msg-success");
		
		pwConfirmMsg.text("").removeClass("msg-error msg-info msg-success");
	});
	
	newPw.on("blur", newPwVal);
	
	/* 비밀번호 재입력창 */
	newPwConfirm.on("input", function() {
		pwConfirmMsg.text("").removeClass("msg-error msg-info msg-success");
	});
	newPwConfirm.on("blur", pwMatch);
	
	/* 각각 보기 버튼 */	
	$("#btnViewCurrent").on("click", function() {togglePasswordVisibility("currentPw"); });
	$("#btnViewNew").on("click", function() {togglePasswordVisibility("newPw"); });
	$("#btnViewConfirm").on("click", function() {togglePasswordVisibility("newPwConfirm"); });
	
	/* 변경하기 버튼 클릭 */
	pwSubmitBtn.on("click", function(e) {
		e.preventDefault(); // submit 방지
		
		// 현재 비밀번호 미입력 시
		if (!currentPwVal()) return;
		
		// 새 비밀번호가 규칙에 어긋날 시
		if (!newPwVal()) {
			newPw.focus();
			return;
		}
		
		// 재입력한 비밀번호가 다를 시
		if (!pwMatch()) {
			newPwConfirm.focus();
			return;
		}
		
		$("#pwForm").submit();
	});
	
	/* 예외처리 */
	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get("error") === "wrong_pw") {
		alert("현재 비밀번호가 일치하지 않습니다. 다시 확인해 주세요");
		currentPw.focus();
	}
	
	if (urlParams.get("status") === "success") {
		alert("비밀번호 변경이 완료되었습니다");
		window.history.replaceState({}, document.title, window.location.pathname);
	}
});




