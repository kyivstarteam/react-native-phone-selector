# react-native-phone-selector

Phone Selector API for Android Only

## Installation

```sh
npm install react-native-phone-selector

or

yarn add react-native-phone-selector
```

## Usage

```js
import PhoneSelector from "react-native-phone-selector";

// ...


export interface PhoneResponse {
  selectedPhone: string
}

const result: PhoneResponse = await PhoneSelector.invokePhoneSelector();
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
