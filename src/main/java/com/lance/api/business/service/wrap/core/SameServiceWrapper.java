package com.lance.api.business.service.wrap.core;


import com.lance.api.business.pojo.model.EntryModel;

/**
 * 判断多个业务是否有相同的servCode,若存在相同情况需继承此类重写此方法，改为return true
 *
 * @author lance
 * @since 2018-10-15
 */
public class SameServiceWrapper
{

    /**
     * 判断是否存在servCode相同的情况
     *
     * @return
     */
    public Boolean hasSameService()
    {
        return false;
    }


    public boolean sameIsMyDo(EntryModel entryModel)
    {
        return false;
    }
}
