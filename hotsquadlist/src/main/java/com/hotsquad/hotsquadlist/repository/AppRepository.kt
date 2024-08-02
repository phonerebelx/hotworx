package com.hotsquad.hotsquadlist.repository

import com.hotsquad.hotsquadlist.koin.InjectUtils

class AppRepository {
    
    var userRepository = InjectUtils.userRepository
}