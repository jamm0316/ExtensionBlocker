import {API_PATH} from "/js/config/pathConfig.js";

export function addCustomExtension(extName) {
  return $.ajax({
    url: `${API_PATH}/custom`,
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify({ name: extName, type: "CUSTOM"}),
    success: function (data) {
      console.log(data);
    },
    error: function (err) {
      console.log(err)
    },
  });
}