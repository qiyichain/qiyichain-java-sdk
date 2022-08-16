package com.qiyichain.utils;

import java.math.BigInteger;

class IBANGenerator {

    public static void main(String[]args){
        System.out.println("IBAN: "+generateIban());


    }

    //generate IBAN code
    private static String generateIban(){
        String codigoPais="XE";
        String entidad = getBank();
        String oficina = String.valueOf((int)Math.floor(Math.random()*(9999-1000+1)+1000));
        String numeroCuenta = String.valueOf((int)Math.floor(Math.random()*(1222222222f-1000000000+1)+1000000000));
        System.out.println("entidad="+entidad);
        System.out.println("oficina="+oficina);
        System.out.println("numerocuenta="+numeroCuenta);
        return getIban(codigoPais,entidad,oficina,numeroCuenta);
    }

    //List of bank numbers
    private static String getBank() {
        String [] banks = {"0136","0238","0130","0038","0132","0133","0233","0232","0231","0036","0138","0239",
                "0235","0234","2095","2096","3095","2013","3045","0131","1546","1544","1545","1535","3121","3127","1549","0144",
                "2401","1465","1467","3190","1460","1463","0049","0125","0122","0121","0031","3183","0046","0128",
                "1490","1491","1492","1493","1494","3130","1496","1499","3138","3035","2415",
                "2105","2416","2038","0113","0059","0058","0115","0057","3140","1482","1481"};
        return banks[(int)Math.floor(Math.random()*((banks.length - 1) +1)+0)];
    }


    //Get iban digit from account data and return the complete IBAN code
    private static String getIban(String codigoPais, String entidad, String oficina, String numeroCuenta){
        String officebankDC = getOfficceBankDC(entidad, oficina);
        String accountDC = getAccountDC(numeroCuenta);
        String dcIban = getIBANDigit(codigoPais, entidad, oficina, officebankDC+accountDC, numeroCuenta);
        return codigoPais+dcIban+entidad+oficina+officebankDC+accountDC+numeroCuenta;
    }

    //Algorithm to calculate IBAN digit
    private static String getIBANDigit(String codigoPais, String entidad, String oficina, String dc, String numeroCuenta){

        String preIban = entidad+
                oficina+
                dc+
                numeroCuenta+
                IBANweight(codigoPais.charAt(0))+
                IBANweight(codigoPais.charAt(1))+
                "00";
        BigInteger ccc = new BigInteger(preIban);
        BigInteger noventaysiete = new BigInteger("97");
        ccc = ccc.mod(noventaysiete);
        int dcIb = ccc.intValue();
        dcIb = 98 - dcIb;
        return setZerosLeft(Integer.toString(dcIb),2);
    }

    //Add padding
    private static String setZerosLeft(String str, int longitud){
        StringBuilder ceros = new StringBuilder();
        if(str.length()<longitud){
            for(int i=0;i<(longitud-str.length());i++){
                ceros.append('0');
            }
            str = ceros + str;
        }

        return str;
    }

    //Iban weight
    private static String IBANweight(char letra){
        String peso = "";
        letra = Character.toUpperCase(letra);
        switch (letra){
            case 'A': peso = "10";
                break;
            case 'B': peso = "11";
                break;
            case 'C': peso = "12";
                break;
            case 'D': peso = "13";
                break;
            case 'E': peso = "14";
                break;
            case 'F': peso = "15";
                break;
            case 'G': peso = "16";
                break;
            case 'H': peso = "17";
                break;
            case 'I': peso = "18";
                break;
            case 'J': peso = "19";
                break;
            case 'K': peso = "20";
                break;
            case 'L': peso = "21";
                break;
            case 'M': peso = "22";
                break;
            case 'N': peso = "23";
                break;
            case 'O': peso = "24";
                break;
            case 'P': peso = "25";
                break;
            case 'Q': peso = "26";
                break;
            case 'R': peso = "27";
                break;
            case 'S': peso = "28";
                break;
            case 'T': peso = "29";
                break;
            case 'U': peso = "30";
                break;
            case 'V': peso = "31";
                break;
            case 'W': peso = "32";
                break;
            case 'X': peso = "33";
                break;
            case 'Y': peso = "34";
                break;
            case 'Z': peso = "35";
                break;
        }
        return peso;
    }

    //Get first digit control
    private static String getOfficceBankDC(String bank, String branch){

        /*
        The first digit is obtained by the following algorithm:
        1. Concatenate the digits of both the bank code and the branch code.
        2. Multiply each digit respectively by: 4, 8, 5, 10, 9, 7, 3 6
        3. Sum all the results into a single integer.
        4. Obtain the modulus 11 (remainder of the division by 11) of the
        integer obtained in 3.
        5. Subtract the remainder from 11 (11 is a magic number).
        Note: if the result of the subtraction is 10, it should be converted to "1".
                If the result of the subtraction is 11, it should be converted to "0".
        */

        String list = bank+branch;
        char[] groupBytes = list.toCharArray();
        int[] groupBytesInt= new int[groupBytes.length];
        int[] magicNumbers = {4, 8, 5, 10, 9, 7, 3, 6};

        for (int i =0; i<groupBytes.length;i++){
            groupBytesInt[i]=(byte)groupBytes[i];
            groupBytesInt[i] = groupBytesInt[i] * magicNumbers[i];

        }
        int result =0;
        for (int aGroupBytesInt : groupBytesInt) {
            result += aGroupBytesInt;
        }
        result = result % 11;
        int digit = 11 - result;

        if (digit == 10){
            digit = 1;
        }else if(digit == 11){
            digit = 0;
        }
        return String.valueOf((digit-1));
    }

    /*
    The first digit is obtained by the following algorithm:
    1. Concatenate the digits of the account number.
    2. Multiply each digit respectively by: 1, 2, 4, 8, 5, 10, 9, 7, 3, 6
    3. Sum all the results into a single integer.
    4. Obtain the modulus 11 (remainder of the division by 11) of the
       integer obtained in 3.
    5. Subtract the remainder from 11 (11 is a magic number).
       Note: if the result of the subtraction is 10, it should be converted to "1".
       If the result of the subtraction is 11, it should be converted to "0".
     */

    //Get second digit control
    private static String getAccountDC(String account){
        byte[] groupBytes = account.getBytes();
        int[] groupBytesInt= new int[groupBytes.length];
        int[] magicNumbers = {1, 2, 4, 8, 5, 10, 9, 7, 3, 6};
        for (int i =0; i<groupBytes.length;i++){
            groupBytesInt[i]=groupBytes[i];
            groupBytesInt[i] = groupBytesInt[i] * magicNumbers[i];

        }
        int result =0;
        for (int aGroupBytesInt : groupBytesInt) {
            result += aGroupBytesInt;
        }
        result = result % 11;
        int digit = 11 -result;

        if (digit == 10){
            digit = 1;
        }else if(digit == 11){
            digit = 0;
        }else{
            digit = digit - 1;
        }

        return String.valueOf(digit);
    }
}
