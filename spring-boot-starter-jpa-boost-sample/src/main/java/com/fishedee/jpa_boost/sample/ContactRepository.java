package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.CurdRepository;
import org.springframework.stereotype.Component;

@Component
public class ContactRepository extends CurdRepository<Contact,Long> {
}
