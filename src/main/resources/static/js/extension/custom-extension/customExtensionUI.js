import {addCustomExtension, deleteCustomExtension} from "/js/extension/custom-extension/customExtension.api.js";
import {sanitize, validationAndAlert} from "/js/extension/common/utils.js";

export function initCustomExtensionUI() {
  $("#custom-add-btn").on("click", () => {
    let ext = sanitize($("#custom-input").val());
    let currentCount = $("#custom-list .badge").length;

    if (!validationAndAlert(ext, currentCount)) return;
    if (!isCustomExtensionDuplicate(ext)) return alert("이미 추가된 확장자입니다.");
    if (focusIfFixedExtension(ext)) return alert("고정 확장자 입니다.");

    addCustomExtension(ext)
      .then(() => location.reload())
      .catch(() => alert("추가에 실패했습니다."));
  })

  $(".remove-btn").on("click", function () {
    let ext = sanitize($(this).data("name"));
    if (!validationAndAlert(ext)) return;

    deleteCustomExtension(ext)
      .then(() => location.reload())
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
  const existingFixedExt = new Set(
    $('.form-check-input').map(function () {
      return sanitize($(this).data("name"));
    }).get()
  );

  if (existingFixedExt.has(ext)) {
    $('.form-check-input').each(function () {
      if (sanitize($(this).data("name")) === ext) {
        $(this).focus();
      }
    });
    return existingFixedExt.has(ext);
  }
}