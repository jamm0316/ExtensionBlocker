import {addCustomExtension} from "/js/extension/custom-extension/api.js";
import {sanitize, validateExtension} from "/js/extension/custom-extension/service.js";

export function initCustomExtensionUI() {
  $("#custom-add-btn").on("click", () => {
    const ext = sanitize($("#custom-input").val());
    const currentCount = $("#custom-list .badge").length;

    const {valid, message} = validateExtension(ext, currentCount);
    if (!valid) {
      alert(message);
      return;
    }

    addCustomExtension(ext)
      .then(() => location.reload())
      .catch(() => alert("추가에 실패했습니다."));
  })
}