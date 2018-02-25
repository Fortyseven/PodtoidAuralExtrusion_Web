package com.hacsoft.thepodtoidauralextrusion;


class SoundBankEntry
{
        public enum SoundBankEntryType
        {
                ENTRY_TYPE_SHORT, ENTRY_TYPE_LONG, ENTRY_TYPE_DIVIDER
        }

        public String             Name;
        public int                Resource_ID;
        public SoundBankEntryType Type;
        public Boolean            IsNew;

        SoundBankEntry(int id, String name, SoundBankEntryType type, Boolean is_new)
        {
                this.Name = name;
                this.Resource_ID = id;
                this.Type = type;
                this.IsNew = is_new;
        }

        SoundBankEntry(int id, String name, SoundBankEntryType type)
        {
                this(id, name, type, false);
        }

        SoundBankEntry(String name)
        {
                this(-1, name, SoundBankEntryType.ENTRY_TYPE_DIVIDER);
//                this.Name = name;
//                this.Resource_ID = -1;
//                this.Type = SoundBankEntryType.ENTRY_TYPE_DIVIDER;
        }
}
