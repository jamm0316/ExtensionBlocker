import {addCustomExtension, deleteCustomExtension} from "/js/extension/custom-extension/customExtension.api.js";
import {sanitize, validateExtension} from "/js/extension/common/utils.js";

function validationAndAlert(ext, count) {
  const {valid, message} = validateExtension(ext, count);
  if (!valid) alert(message);
  return valid;
}

export function initCustomExtensionUI() {
  $("#custom-add-btn").on("click", () => {
    let ext = sanitize($("#custom-input").val());
    let currentCount = $("#custom-list .badge").length;

    if (!validationAndAlert(ext, currentCount)) return;

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