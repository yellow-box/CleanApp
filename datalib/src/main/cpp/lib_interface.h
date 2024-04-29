#ifndef LIB_INTERFACE_H
#define LIB_INTERFACE_H
#define size_t long long
const char* get_lib_version();
extern "C" unsigned char* aes_enc(const char* key, const char* iv, const unsigned char* data, size_t data_len);
extern "C" unsigned char* aes_dec(const char* key, const char* iv, const unsigned char* data, size_t data_len);
extern "C" unsigned char* rsa_enc(const char* pub_pem, const unsigned char* data, size_t data_len);
extern "C" unsigned char* rsa_dec(const char* pri_pem, const unsigned char* data, size_t data_len);
void rsa_keygen(const char* pri_pem, const char* pub_pem);

#endif