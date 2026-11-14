$(function(){
	/*** [DOM 요소]***/
	const ipid = $("#userid");
	const ippw = $("#userpw");
	const logForm = $("#logForm");
	const errorMsg = $("#loginErrorMsg");
	
	/*** [UI,UX] ***/
	/*** [로그인 실패시], [리뷰작성에서 로그인 안되있을시] ***/
	//authcontroller 에서 msg=fail
	//reviewcontroller 에서 msg=need_login
	const urlParams = new URLSearchParams(window.location.search);
	const msg = urlParams.get("msg");
	if(msg)
	{
		switch(msg)
			{
				case "fail"	: 
					errorMsg.text("아이디 또는 비밀번호가 일치하지 않습니다.");
					break;
				case "need_login" :
					alert("로그인이 필요한 서비스입니다");
					break;
			}
			history.replaceState({}, document.title, window.location.pathname);
	}
	/***1. 페이지 최초 로드시 ***/
	ipid.focus();
	/***2. id 관련 ***/
	ipid.on("input", function(){
		errorMsg.text("");
	});
	ipid.on("blur", function(){
		const id = ipid.val().trim();
		if(id === "")
		{
			errorMsg.text("아이디를 입력하세요");
		}
	});
	/***3. pw 관련 ***/
	ippw.on("keyup", function(e){
		//originalEvent 미지원 브라우저or기기 환경 안전장치(스크릅티 중단방지)
		const originalEvent = e.originalEvent;
		if(originalEvent && typeof originalEvent.getModifierState === "function")
		{
			const cpsLck = originalEvent.getModifierState("CapsLock");
			if(cpsLck)
			{
				errorMsg.text("CapsLock이 켜져있습니다");
				return;
			}
		}
		//cpsLck 꺼져있을때 기존메세지 정리
		if(errorMsg.text() === "CapsLock이 켜져있습니다")
		{
			errorMsg.text("");
		}
	});
	ippw.on("blur", function(){
		const pw = ippw.val().trim();
		if(pw === "")
		{
			errorMsg.text("비밀번호를 입력하세요");
		}
	});
	
	/*** [로그인 클릭시] 최종 유효성 검사 ***/
	logForm.on("submit", function(e){
		const id = ipid.val().trim();
		const pw = ippw.val().trim();
		if(id === "")
		{
			e.preventDefault();
			errorMsg.text("아이디를 입력하세요");
			ipid.focus();
			return;
		}
		if(pw === "")
		{
			e.preventDefault();
			errorMsg.text("비밀번호를 입력하세요");			
			ippw.focus();
			return;
		}
	});
	
});