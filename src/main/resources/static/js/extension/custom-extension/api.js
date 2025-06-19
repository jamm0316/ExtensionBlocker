import {API_PATH} from "/js/config/pathConfig.js";

export function addCustomExtension(extName) {
  return $.ajax({
    url: `${API_PATH}/custom`,
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify({ name: extName, type: "CUSTOM"}),
  });
}

export function deleteCustomExtension(extName) {
  return $.ajax({
    url: `${API_PATH}/custom/${extName}`,
    method: "DELETE",
    contentType: "application/json",
    data: JSON.stringify({name: extName})
  })
}