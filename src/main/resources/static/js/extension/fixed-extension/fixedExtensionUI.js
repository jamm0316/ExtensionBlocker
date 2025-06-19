import { toggleExtensionBan } from "/js/extension/fixed-extension/fixedExtension.api.js";
import { validationAndAlert, sanitize } from "/js/extension/common/utils.js";

export function initFixedExtensionUI() {
  $(".form-check-input").on("click", function () {
    let ext = sanitize($(this).data("name"));
    if (!validationAndAlert(ext)) return;

    toggleExtensionBan(ext)
      .catch(() => alert("토글에 실패했습니다."))
  });
}
