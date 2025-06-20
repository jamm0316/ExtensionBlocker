import {addCustomExtension, deleteCustomExtension} from "/js/extension/custom-extension/customExtension.api.js";
import {sanitize, validationAndAlert} from "/js/extension/common/utils.js";

export function initCustomExtensionUI() {
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