$(function(){
	/*** [DOM 요소]***/
	/*** 1. 아이디 찾기 관련 ***/
	const FIemailid = $("#FIemailid");
	const FIemaildomain = $("#FIemaildomain");
	const FIemailDomainSelect = $("#FIemailDomainSelect");
	const FIsendEmailCodeBtn = $("#FIsendEmailCode");
	const FIemailcode = $("#FIemailcode");
	const sendIdBtn = $("#sendIdBtn");
	
	/*** 2. 임시비밀번호 발급 관련 ***/
	const userid = $("#userid");
	const FPemailid = $("#FPemailid");
	const FPemaildomain = $("#FPemaildomain");
	const FPemailDomainSelect = $("#FPemailDomainSelect");
	const FPsendEmailCodeBtn = $("#FPsendEmailCode");
	const FPemailcode = $("#FPemailcode");
	const FPsendTmpPwBtn = $("#FPsendTmpPwBtn");
	
	/*** [상태 플래그 변수] ***/
	/***1. 아이디 찾기 관련***/
	let isEmailVerified = false;
	/***2. 임시비밀번호 발급 관련***/
	let isEmailVerified2 = false;
	/***3. 이메일 인증번호 발송 관련 ***/
	let isSendEmail = false; // 메일 발송중인가?
	/***3. 이메일 인증번호 발송 관련 ***/
	let isSendEmail2 = false; // 메일 발송중인가?
	
	/*** [UI, UX] ***/
	/***1. 페이지 최초 로드시 ***/
	FIemailid.focus();
	
	/***2. 아이디찾기 관련 ***/
	/***2-1. ipemail 초기화 및 emailselect 관련*/
	FIemailid.on("input", function() {
		isEmailVerified = false;
	});
	
	FIemaildomain.on("input", function() {
		isEmailVerified = false;
	});	
	
	FIemailDomainSelect.on("change", function(){
		const selectVal = $(this).val();
		if(selectVal === "")
		{
			FIemaildomain.val("").prop("readonly", false).focus();
		}else{
			FIemaildomain.val(selectVal).prop("readonly", true);
		}
	});
	
	/***2-2 이메일 인증번호 발송 관련 ***/
	FIsendEmailCodeBtn.on("click", function() {
	    // 이메일 아이디
	    let emailId = FIemailid.val().trim();
	    // 이메일 도메인
	    let emailDomain = FIemaildomain.val().trim();
		
		if(isSendEmail === true)
		{
			alert("인증코드가 발송중입니다. 기다려주세요..");
			return;
		}
	    console.log("인증번호 발송 버튼 클릭됨");
	
	    // 이메일 입력 확인
	    if (emailId === "" || emailDomain === "") 
	    {
	        alert("이메일을 입력해주세요.");
	        FIemailid.focus();
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
	            emailtype : "id찾기"
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
	
	/***2-3 이메일 인증번호 확인 ***/
	sendIdBtn.on("click", function(){
		let code = FIemailcode.val().trim();
		let emailId = FIemailid.val().trim();
		let emailDomain = FIemaildomain.val().trim();
		if(code === "") 
		{
			alert("인증번호를 입력해주세요.");
			FIemailcode.focus();
			return;
		}

		$.ajax({
			url: contextPath + "/codecheck",
			type: "POST",
			data: {
				emailid : emailId,
				emaildomain : emailDomain,
				emailcodeid : code,
				emailtype : "id찾기"
			},
			dataType: "json",
			success: function(result) {
				if(result.msg === "OK") {
					alert("이메일 인증이 완료되었습니다.");
					isEmailVerified = true;
					// 인증 완료 후 이메일 수정 방지
					FIemailid.prop("readonly", true);
					FIemaildomain.prop("readonly", true);
					FIemailDomainSelect.prop("disabled", true);
					FIemailcode.prop("readonly", true);
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
	
	/***3. 임시비밀번호 관련 ***/
	/***3-1 ipemail 초기화 및 emailselect 관련 ***/
	FPemailid.on("input", function() {
		isEmailVerified2 = false;
	});
	
	FPemaildomain.on("input", function() {
		isEmailVerified2 = false;
	});	
	
	FPemailDomainSelect.on("change", function(){
		const selectVal = $(this).val();
		if(selectVal === "")
		{
			FPemaildomain.val("").prop("readonly", false).focus();
		}else{
			FPemaildomain.val(selectVal).prop("readonly", true);
		}
	});
	
	/***3-2 이메일 인증번호 발송 관련 ***/
	FPsendEmailCodeBtn.on("click", function() {
		//사용자 아이디
		let ipid = userid.val().trim();
	    // 이메일 아이디
	    let emailId = FPemailid.val().trim();
	    // 이메일 도메인
	    let emailDomain = FPemaildomain.val().trim();
		
		if(isSendEmail2 === true)
		{
			alert("인증코드가 발송중입니다. 기다려주세요..");
			return;
		}
	    console.log("인증번호 발송 버튼 클릭됨");
	
		if(ipid === "")
		{
			alert("아이디를 입력해주세요");
			userid.focus();
			return;
		}	
	
	    // 이메일 입력 확인
	    if (emailId === "" || emailDomain === "") 
	    {
	        alert("이메일을 입력해주세요.");
	        FPemailid.focus();
	        return;
	    }
	
		//$("#sendMailMsg").html("인증코드 발송중입니다. 잠시만 기다려 주세요..");
		isSendEmail2 = true;
	    $.ajax({
	        url: contextPath + "/sendemail",
	        type: "POST",
	        data: 
	        {
	            emailid : emailId,
	            emaildomain : emailDomain,
	            emailtype : "pw찾기"
	        },
	        dataType: "json",
	        success: function(result) {
				isSendEmail2 = false;
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
	
	/***3-3 이메일 인증번호 확인 및 임시번호 발송 ***/
	FPsendTmpPwBtn.on("click", function(){
		let ipid = userid.val().trim();
		let code = FPemailcode.val().trim();
		let emailId = FPemailid.val().trim();
		let emailDomain = FPemaildomain.val().trim();
		
		if(isSendEmail2 === true)
		{
			alert("인증코드가 발송중입니다. 기다려주세요..");
			return;
		}
	
		if(ipid === "")
		{
			alert("아이디를 입력해주세요");
			userid.focus();
			return;
		}	
	
	    if (emailId === "" || emailDomain === "") 
	    {
	        alert("이메일을 입력해주세요.");
	        FPemailid.focus();
	        return;
	    }		
		
		if(code === "") 
		{
			alert("인증번호를 입력해주세요.");
			FPemailcode.focus();
			return;
		}

		$.ajax({
			url: contextPath + "/codecheck",
			credentials: 'include',				//세션 정보 전달하기 위해 사용
			type: "POST",
			data: {
				userid : ipid,
				emailid : emailId,
				emaildomain : emailDomain,
				emailcodepw : code,
				emailtype : "pw찾기"
			},
			dataType: "json",
			success: function(result) {
				if(result.msg === "OK") {
					alert("임시번호가 발송되었습니다.");
					isEmailVerified2 = true;
					// 인증 완료 후 이메일 수정 방지
					FPemailid.prop("readonly", true);
					FPemaildomain.prop("readonly", true);
					FPemailDomainSelect.prop("disabled", true);
					FPemailcode.prop("readonly", true);
				} else {
					alert("인증번호가 일치하지 않거나 만료되었습니다.");
					isEmailVerified2 = false;
				}
			},
			error: function(xhr, status, error) {
				console.error(xhr.responseText);
				alert("인증 확인 중 오류가 발생했습니다.");
			}
		});
	});
});