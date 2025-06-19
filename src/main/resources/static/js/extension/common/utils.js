export function sanitize(input) {
  return input.trim().toLowerCase();
}

export function validateExtension(ext, count) {
  if (!ext || ext.length === 0) return { valid:false, message: "확장자 명을 입력하세요." }
  if (count >= 200) return { valid:false, message: "최대 200개까지 등록할 수 있습니다." }
  return {valid: true}
}

export function validationAndAlert(ext, count) {
  const {valid, message} = validateExtension(ext, count);
  if (!valid) alert(message);
  return valid;
}