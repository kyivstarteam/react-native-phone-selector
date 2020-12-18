declare module '@kyivstarteam/react-native-phone-selector' {
  export interface PhoneResponse {
    selectedPhone: string;
  }
  class PhoneSelector {
    static invokePhoneSelector(): Promise<PhoneResponse>;
  }

  export default PhoneSelector;
}
