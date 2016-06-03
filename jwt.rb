require 'jwt'


exp = Time.now.to_i + 8 * 60

iat = Time.now.to_i

header = {:typ => 'JOSE', :kid => 'values'}

payload ={:sub => '1234567890', :name => 'John Doe', :admin => true, :iat => iat, :exp => exp}

token = JWT.encode payload, 'secret', 'HS256', header

puts token

#token = 'eyJhbGciOiAiSFMyNTYiLCJ0eXAiOiAiSldUIn0.eyJzdWIiOiAiMTIzNDU2Nzg5MCIsIm5hbWUiOiAiSm9obiBEb2UiLCJhZG1pbiI6IHRydWV9.aQ0erWgnej3f__Jz3NehlH1VB8fYdtYeeCqHuvhbzC4'

# Set password to nil and validation to false otherwise this won't work
decoded_token = JWT.decode token, 'secret', true, { :algorithm => 'HS256'}

puts decoded_token
