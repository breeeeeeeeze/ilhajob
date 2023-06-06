/**
 * awards
 */
 
// addAwards() : 새로운 경력 영역을 추가
function addAwards() {
	// 새로운 요소를 생성하고 클래스 이름을 추가
	let newAwardsBlock = document.createElement("div");
	newAwardsBlock.classList.add("awards-block");
	
	// 새로운 요소의 내부 HTML을 설정
	newAwardsBlock.innerHTML = `
    <div class="resume-block">
    <div class="inner" id="newAwardsInner">
      <span class="name">A</span>
      <div class="title-box">
      	<div class="edit-btns col-lg-11 col-md-11"></div>
          <div class="edit-btns col-lg-1 col-md-1">
          	<input type="hidden" name="awardsId">
          	<button type="button" onclick="createAwards()"><span class="la la-check-circle-o"></span></button>
          </div>
         </div>
      <div class="row">
      	<div class="col-lg-6 col-md-6 form-group">
      	<label>수상명</label>
      	<input type="text" class="text" name="awardsName" id="awardsName" placeholder="수상명을 입력하세요.">
          <span></span>
      	</div>
      	<div class="col-lg-6 col-md-6 form-group">
      	<label>수상 상세</label>
      	<input type="text" class="text" name="awardsContent" id="awardsContent" placeholder="수상의 상세내용을 입력하세요">
      	</div>
      	
        <div class="edit-box">
        <div class="form-group col-lg-12 col-md-12">
        <label>수상일</label><br>
        <input type="date" class="year" id="awardsDate" name="awardsDate">
        </div>
        </div>
      </div> <!-- row end -->
      </div>
  </div>
  `;

	let awardsList = document.getElementById("awards-block");
	console.log(">>>>>>>>>> 추가영역 생성 성공 " + awardsList);
	let lastAwardsBlock = awardsList.lastElementChild;
	awardsList.insertBefore(newAwardsBlock, lastAwardsBlock.nextSibling);

	let rect = document.getElementById("newAwardsInner").getBoundingClientRect();
	window.scrollBy(0, rect.top - 110);
}

function createAwards() {
	let awardsName = $("#awardsName").val();
	let awardsContent = $("#awardsContent").val();
	let awardsDate = $("#awardsDate").val();

	if (awardsName == "") {
		alert("수상명을 입력하세요.");
		$("#awardsName").focus();
		return false;
	}
	if (awardsContent == "") {
		alert("수상의 상세 내용을 입력하세요.");
		$("#awardsContent").focus();
		return false;
	}
	if (awardsDate == "") {
		alert("수상한 날짜를 입력하세요.");
		$("#awardsDate").focus();
		return false;
	}

	$.ajax({
		url: "awards",
		type: "POST",
		data: {
			awardsName: awardsName,
			awardsContent: awardsContent,
			awardsDate: awardsDate
		},
		success: function(response) {
			console.log("success");
		},
		error: function(xhr, status, error) {
			console.log("에러 발생: " + error);
			console.log("상태 코드: " + xhr.status);
			console.log("에러 메시지: " + xhr.responseText);
		}
	}).done(function(fragment) {
		$('#awards-block').replaceWith(fragment);
	});
}

function editAwards(awardsId) {
	$('#awardsId' + awardsId).val(awardsId);
	const awardsBlock = document.querySelector(`#awardsBlock${awardsId}`);
	const inputs = awardsBlock.querySelectorAll('input[type="text"]');
	inputs.forEach(input => input.removeAttribute('disabled'));
	awardsBlock.querySelectorAll('input[type="date"]').forEach(input => input.style.display = 'block');
	awardsBlock.querySelectorAll('.year-span').forEach(span => span.style.display = 'none');
}

function updateAwards(awardsId) {
	let awardsName = $("#awardsName" + awardsId).val();
	let awardsContent = $("#awardsContent" + awardsId).val();
	let awardsDate = $("#awardsDate" + awardsId).val();

	if (awardsName == "") {
		alert("수상명을 입력하세요.");
		$("#awardsName" + awardsId).focus();
		return false;
	}
	if (awardsContent == "") {
		alert("수상의 상세 내용을 입력하세요.");
		$("#awardsContent" + awardsId).focus();
		return false;
	}
	if (awardsDate == "") {
		alert("수상한 날짜를 입력하세요.");
		$("#awardsDate" + awardsId).focus();
		return false;
	}

	$.ajax({
		url: "awards/" + awardsId,
		type: "PUT",
		data: {
			awardsName: awardsName,
			awardsContent: awardsContent,
			awardsDate: awardsDate,
			awardsId: awardsId
		},
		success: function() {

		},
		error: function() {

		}
	}).done(function(fragment) {
		$('#awards-block').replaceWith(fragment);
	});
}

function deleteAwards(awardsId) {
	//console.log(">>> deleteAwards(awardsId) : " + awardsId);
	$('#awardsId'  + awardsId).val(awardsId);
	console.log($('#awardsId' + awardsId).attr('value'));

	$.ajax({
		url: "awards/" + awardsId,
		type: "DELETE",
		data: $('.default-form').serialize(),
		success: function() {
			console.log("success")
		},
		error: function() {
			console.log("fail")
		}
	}).done(function(fragment) {
		$('#awards-block').replaceWith(fragment);
	});
}

