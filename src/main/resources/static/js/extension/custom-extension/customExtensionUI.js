import {addCustomExtension, deleteCustomExtension} from "/js/extension/custom-extension/customExtension.api.js";
import {sanitize, validationAndAlert} from "/js/extension/common/utils.js";

export function initCustomExtensionUI() {
  //커스텀 확장자 추가 버튼 클릭 시 +1
  $(document).on("click", "#custom-add-btn", () => {
    let ext = sanitize($("#custom-input").val());
    let currentCount = $("#custom-list .badge").length;

    if (!validationAndAlert(ext, currentCount)) return;
    if (!isCustomExtensionDuplicate(ext)) return alert("이미 추가된 확장자입니다.");
    if (focusIfFixedExtension(ext)) return alert("고정 확장자 입니다.");

    addCustomExtension(ext)
      .then(() => {
        $("#custom-list").append(customChip(ext));
        $("#custom-input").val("");

        let currentCount = parseInt($("#custom-count").text(), 10);
        $("#custom-count").text(currentCount + 1);
      })
      .catch(() => alert("추가에 실패했습니다."));
  })

  //커스텀 확장자 삭제 버튼 클릭 시 -1
  $(document).on("click", ".remove-btn", function () {
    let ext = sanitize($(this).data("name"));
    if (!validationAndAlert(ext)) return;

    deleteCustomExtension(ext)
      .then(() => {
        $(this).closest(".badge").remove();
        const currentCount = parseInt($("#custom-count").text(), 10);
        $("#custom-count").text(currentCount - 1);
      })
      .catch(() => alert("삭제에 실패했습니다."))
  })
}

//실시간 valuedation 체크
$(document).ready(function () {
  const $input = $('#custom-input');

  // 입력값 제어 (실시간)
  $input.on('input', function () {
    let value = $(this).val();
    const filtered = value.replace(/[^a-z0-9]/gi, '');  // 영문 대소문자/숫자만 허용
    $(this).val(filtered.toLowerCase());
  });

  // 포커스 아웃 시 유효성 체크 (선택)
  $input.on('blur', function () {
    const value = $(this).val();
    const valid = /^[a-z0-9]+$/.test(value) && !/^[0-9]+$/.test(value);

    if (!valid) {
      $(this).addClass('is-invalid');
    } else {
      $(this).removeClass('is-invalid');
    }
  });
});

function isCustomExtensionDuplicate(ext) {
  const existingCustomExt = new Set(
    $('.blocked-custom-extension-name').map(function () {
      return $(this).text().toLowerCase();
    }).get()
  );

  return !existingCustomExt.has(ext);
}

function focusIfFixedExtension(ext) {
  const fixedExtensionList = $('.form-check-input');
  const existingFixedExt = new Set(
    fixedExtensionList.map(function () {
      return sanitize($(this).data("name"));
    }).get()
  );

  if (existingFixedExt.has(ext)) {
    fixedExtensionList.each(function () {
      if (sanitize($(this).data("name")) === ext) {
        $(this).focus();
      }
    });
    return existingFixedExt.has(ext);
  }
}

function customChip(extName) {
  return $(`
  <div>
    <div class="badge bg-secondary rounded-pill px-3 py-2" id="chip-list">
      <span class="blocked-custom-extension-name">${extName}</span>
      <button type="button"
              class="btn-close btn-close-white ms-2 remove-btn"
              data-name="${extName}"
              aria-label="Remove"></button>
    </div>
  </div>
    `);
}