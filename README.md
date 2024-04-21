# RSA-Digital-Signature

- Implement a digital signature program using RSA encryption algorithms and SHA-1 hash functions
- This work is a mini-project under the course IT4015 - Introduction to Cryptography and Security

## Setting

- Clone repositpry : git clone "path"
- Open foler on your text editor or ide 

## Usage

- You need to create res directory

### SHA-1

- Make a .txt file store hashing document in res directory
- This class I have not yet completed the file reading and writing section, which will be completed one day

### Sign document

- Go to the application directory
- Compile it - 'javac Sign.java' (for the first time run)
- Make a signature on a document - 'java Sign filename'

### Verify a signature

- Compile it - 'javac Verify.java' (for the first time run)
- Make a signature on a document - 'java Verify filename signature_filename'
- Example 'D:\RSA-Digital-Signature\src> java Verify doc.txt signed20240421184922.txt'
