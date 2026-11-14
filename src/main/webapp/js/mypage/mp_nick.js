$(function() {
	/* [닉네임 정규식] */
	const nickRegex = /^[a-zA-Z0-9가-힣_]{3,20}$/; // 회원가입때 사용한 닉네임 정규식
	
	/* 선택자 변수 */
	const inputNick = $("#newNick"); // 새 닉네임 입력
	const nickSubmitBtn = $("#btnSubmitNick"); // 닉네임 변경 버튼
	let isSubmitting = false;
	
	// 중복 체크 버튼 클릭 시 출력할 메시지
	const nickCheckMsg = $("#nickCheckMsg");
	
	/* 함수들 */
	// 유효성 검사
	function nickVal(isButtonClick = false) {
		const nick = inputNick.val().trim();
		// 입력창 클래스 초기화
		nickCheckMsg.removeClass("msg-error msg-info msg-success");
		
		if (nick==="") {
			if (isButtonClick === true) {
				nickCheckMsg.text("닉네임을 입력하세요").addClass("msg-error");
			} else {
				nickCheckMsg.text("");
			}
			return false;
		}
		if (!nickRegex.test(nick)) {
			nickCheckMsg.text("공백 불가, 특수문자는 (_)만 가능, 3~20자").addClass("msg-error");
			return false;
		}
		nickCheckMsg.text("올바른 형식입니다. 변경하기 버튼을 누르면 중복체크 후 변경됩니다.").addClass("msg-success");
		return true;
	}
	
	/* 페이지 첫 입장시 */
	inputNick.focus();
	
	/* 입력을 시작할 때 */
	inputNick.on("input", function() {
		isSubmitting = false;
		nickVal(false);
	});
	
	/* 입력 중 포커스를 잃었을 때 */
	inputNick.on("blur", function() {
		if(isSubmitting) return;
		nickVal(false);
	});
	
	/* 변경하기 버튼 클릭 */
	nickSubmitBtn.on("click", function(e) {
		e.preventDefault(); // submit 방지	
		
		isSubmitting = true;	
		
		if(!nickVal(true)) {
			inputNick.focus();
			return;
		}
		
		const nick = inputNick.val().trim();
		
		// 변경하기 연속으로 누르기 방지
		nickSubmitBtn.prop("disabled", true);
		
		// 백엔드로 파라미터 전송
		$.ajax({
			url : contextPath + "/mp_nick_check",
			type : "GET",
			data : {nick : nick},
			success : function(result) {
				nickCheckMsg.removeClass("msg-error msg-info msg-success");
				
				if (result.trim() === "true") {
					nickCheckMsg.text("사용가능한 닉네임입니다. 변경 처리 중...").addClass("msg-success");
					
					setTimeout(function() {
						$("#nickForm").submit();
					}, 800);
					
				} else {
					nickCheckMsg.text("이미 사용중인 닉네임입니다").addClass("msg-error");
					inputNick.focus();
					nickSubmitBtn.prop("disabled", false);
					isSubmitting = false;
				}
			},
			error : function(err) {
				console.error(err);
				alert("중복 체크 중 에러가 발생했습니다.");
				nickSubmitBtn.prop("disabled", false);
				isSubmitting = false;
			}
		});		
	});
	
	const urlParams = new URLSearchParams(window.location.search);
	
	if (urlParams.get("status") === "success") {
		alert("닉네임 변경이 완료되었습니다.");
		
		window.history.replaceState({}, document.title, window.location.pathname);
	}
});