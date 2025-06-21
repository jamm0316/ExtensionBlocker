export function sanitize(input) {
  return input.trim().toLowerCase();
}

export function validateExtension(ext, count) {
  const MAX_EXTENSION_NAME_LENGTH = 20;  //최대 확장자 이름 길이
  const MAX_EXTENSION_LIMIT = 200;  //최대 확장자 개수
  const VALID_EXTENSION_PATTERN_STRING_INTEGER = /^[a-z0-9]+$/; // 유효한 확장자 이름 패턴
  const ONLY_INTEGER_PATTERN = /^[0-9]+$/; // 유효한 확장자 이름 패턴

  let extLowerCase = sanitize(ext);

  if (!extLowerCase || extLowerCase.length === 0) return {
    valid: false, message: "확장자 명을 입력하세요."
  }
  if (!VALID_EXTENSION_PATTERN_STRING_INTEGER.test(extLowerCase)) return {
    valid: false, message: "영어 소문자와 숫자만 입력 가능합니다."
  }
  if (ONLY_INTEGER_PATTERN.test(extLowerCase)) return {
    valid: false, message: "숫자만 입력은 불가능합니다."
  }
  if (ext.length > MAX_EXTENSION_NAME_LENGTH) return {
    valid: false, message: "20자 이내로 작성해주세요."
  }
  if (count >= MAX_EXTENSION_LIMIT) return {
    valid: false, message: "최대 200개까지 등록할 수 있습니다."
  }
  return {valid: true}
}

export function validationAndAlert(ext, count) {
  const {valid, message} = validateExtension(ext, count);
  if (!valid) alert(message);
  return valid;
}