import { API_PATH } from "/js/config/pathConfig.js";

export function toggleExtensionBan(extName) {
  return $.ajax({
    url: `${API_PATH}/${extName}/ban/toggle`,
    method: "PATCH",
    contentType: "application/json",
    data: JSON.stringify({name: extName}),
  });
}