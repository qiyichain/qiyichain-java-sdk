package com.qiyichain.utils.crypto;

import com.qiyichain.utils.crypto.encode.Bech32;
import com.qiyichain.utils.crypto.encode.ConvertBits;
import com.qiyichain.utils.exception.AddressFormatException;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

public class AddressConvertUtil {

    private static final String qiyichain = "qiyichain";
    private static final String ex = "qyc";

    // nothing to do with prefix
    public static String convertFromBech32ToHex(String address) {
        return convertAddressFromBech32ToHex(address);
    }

    public static String convertFromHexToOkexchainBech32(String address) {
        return convertAddressFromHexToBech32(qiyichain, address);
    }

    public static String convertFromHexToExBech32(String address) {
        return convertAddressFromHexToBech32(ex, address);
    }

    public static String convertFromExBech32ToOkexchainBech32(String address) {
        String hexAddress = convertAddressFromBech32ToHex(address);
        return convertAddressFromHexToBech32(qiyichain, hexAddress);
    }

    public static String convertFromOkexchainBech32ToExBech32(String address) {
        String hexAddress = convertAddressFromBech32ToHex(address);
        return convertAddressFromHexToBech32(ex, hexAddress);
    }

    public static String convertFromOkexchainValToExVal(String address) {
        String hexAddress = convertAddressFromBech32ToHex(address);
        return convertAddressFromHexToBech32(ex+"valoper", hexAddress);
    }

    public static String convertAddressFromHexToBech32(String prefix, String hexAddress) {
        byte[] address = Numeric.hexStringToByteArray(hexAddress);

        String bech32Address = null;
        try {
            byte[] bytes = encode(0, address);
            bech32Address = Bech32.encode(prefix, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bech32Address;
    }

    public static String convertAddressFromBech32ToHex(String bech32Address){
        String hexAddress = null;
        try {
            byte[] bytes = decodeAddress(bech32Address);
            hexAddress = Numeric.toHexString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Keys.toChecksumAddress(hexAddress);
    }


    private static byte[] decodeAddress(String address){
        byte[] dec = Bech32.decode(address).getData();
        return ConvertBits.convertBits(dec, 0, dec.length, 5, 8, false);
    }


    private static byte[] encode(int witnessVersion, byte[] witnessProgram) throws AddressFormatException {
        byte[] convertedProgram = ConvertBits.convertBits(witnessProgram, 0, witnessProgram.length, 8, 5, true);
        return convertedProgram;
    }


}
