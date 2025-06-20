export function sanitize(input) {
  return input.trim().toLowerCase();
}

export function validateExtension(ext, count) {
  let extLowerCase = sanitize(ext);

  if (!extLowerCase || extLowerCase.length === 0) return { valid:false, message: "확장자 명을 입력하세요." }
  if (!/^[a-z0-9]+$/.test(extLowerCase)) return { valid: false, message: "확장자는 영어 소문자만 입력 가능합니다." }
  if (ext.length > 20) return { valid: false, message: "확장자 명은 20자 이내로 작성해주세요." }
  if (count >= 200) return { valid:false, message: "최대 200개까지 등록할 수 있습니다." }
  return {valid: true}
}

export function validationAndAlert(ext, count) {
  const {valid, message} = validateExtension(ext, count);
  if (!valid) alert(message);
  return valid;
}